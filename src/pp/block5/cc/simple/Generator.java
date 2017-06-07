package pp.block5.cc.simple;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import org.antlr.v4.runtime.tree.TerminalNode;
import pp.block5.cc.pascal.SimplePascalBaseVisitor;
import pp.block5.cc.pascal.SimplePascalParser;
import pp.iloc.Simulator;
import pp.iloc.model.*;

import java.util.HashMap;
import java.util.Map;

/** Class to generate ILOC code for Simple Pascal. */
public class Generator extends SimplePascalBaseVisitor<Op> {
	/** The representation of the boolean value <code>false</code>. */
	public final static Num FALSE_VALUE = new Num(Simulator.FALSE);
	/** The representation of the boolean value <code>true</code>. */
	public final static Num TRUE_VALUE = new Num(Simulator.TRUE);

	private int labelCounter;

	/** The base register. */
	private Reg arp = new Reg("r_arp");
	/** The outcome of the checker phase. */
	private Result checkResult;
	/** Association of statement nodes to labels. */
	private ParseTreeProperty<Label> labels;
	/** The program being built. */
	private Program prog;
	/** Register count, used to generate fresh registers. */
	private int regCount;
	/** Association of expression and target nodes to registers. */
	private ParseTreeProperty<Reg> regs;

	/** Generates ILOC code for a given parse tree,
	 * given a pre-computed checker result.
	 */
	public Program generate(ParseTree tree, Result checkResult) {
		this.prog = new Program();
		this.checkResult = checkResult;
		this.regs = new ParseTreeProperty<>();
		this.labels = new ParseTreeProperty<>();
		this.regCount = 0;
		this.labelCounter = 0;
		tree.accept(this);
		return this.prog;
	}

	@Override
	public Op visitProgram(SimplePascalParser.ProgramContext ctx) {
		return visit(ctx.body());
	}

	@Override
	public Op visitBody(SimplePascalParser.BodyContext ctx) {
	    return visit(ctx.block());
	}

    @Override
    public Op visitBlock(SimplePascalParser.BlockContext ctx) {
        Op result = visit(ctx.stat(0));
        for (int i = 1; i < ctx.stat().size(); i++) {
            visit(ctx.stat(i));
        }
        return result;
    }

    // ====================== \\
    //    STATS START HERE    \\
    // ====================== \\

    @Override
    public Op visitAssStat(SimplePascalParser.AssStatContext ctx) {
	    Op result = visit(ctx.expr());
        emit(OpCode.storeAI, reg(ctx.expr()), arp, getOffset(ctx.target()));
        return result;
    }

    @Override
    public Op visitIfStat(SimplePascalParser.IfStatContext ctx) {
	    Op result = visit(ctx.expr());
	    Label thenLabel  = getUniqueLabel("then");
        Label elseLabel  = getUniqueLabel("else");
	    Label endIfLabel = getUniqueLabel("endIf");
	    if (ctx.stat().size() == 1) {
	        emit(OpCode.cbr, reg(ctx.expr()), thenLabel, endIfLabel);
	        emit(thenLabel, OpCode.nop);
	        visit(ctx.stat(0));
            emit(endIfLabel, OpCode.nop);
        } else {
            emit(OpCode.cbr, reg(ctx.expr()), thenLabel, elseLabel);
            emit(thenLabel, OpCode.nop);
            visit(ctx.stat(0));
            emit(OpCode.jumpI, endIfLabel);
            emit(elseLabel, OpCode.nop);
            visit(ctx.stat(1));
            emit(endIfLabel, OpCode.nop);
        }
        return result;
    }

    @Override
    public Op visitWhileStat(SimplePascalParser.WhileStatContext ctx) {
	    Label whileLabel = getUniqueLabel("while");
	    Label whileBody = getUniqueLabel("whileBody");
	    Label endWhileLabel = getUniqueLabel("endWhile");
	    Op result = emit(whileLabel, OpCode.nop);
        visit(ctx.expr());
        emit(OpCode.cbr, reg(ctx.expr()), whileBody, endWhileLabel);
        emit(whileBody, OpCode.nop);
        visit(ctx.stat());
        emit(OpCode.jumpI, whileLabel);
        emit(endWhileLabel, OpCode.nop);
	    return result;
    }

    @Override
    public Op visitBlockStat(SimplePascalParser.BlockStatContext ctx) {
        return visit(ctx.block());
    }

    @Override
    public Op visitInStat(SimplePascalParser.InStatContext ctx) {
        String string = ctx.STR().getSymbol().getText();
	    Op result = emit(OpCode.in, new Str(string.substring(1,string.length()-1)), reg(ctx));
	    emit(OpCode.storeAI, reg(ctx), arp, getOffset(ctx.target()));
        return result;
    }

    @Override
    public Op visitOutStat(SimplePascalParser.OutStatContext ctx) {
	    Op result = visit(ctx.expr());
	    String string = ctx.STR().getSymbol().getText();
	    emit(OpCode.out, new Str(string.substring(1,string.length()-1)), reg(ctx.expr()));
        return result;
    }

    // ====================== \\
    // EXPRESSIONS START HERE \\
    // ====================== \\


    @Override
    public Op visitPrfExpr(SimplePascalParser.PrfExprContext ctx) {
        Op result = visit(ctx.expr());
	    if (ctx.prfOp().MINUS() != null) {
            emit(OpCode.rsubI, reg(ctx.expr()), new Num(0), reg(ctx));
        } else {
            emit(OpCode.rsubI, reg(ctx.expr()), new Num(-1), reg(ctx));
        }
        return result;
    }

    @Override
    public Op visitMultExpr(SimplePascalParser.MultExprContext ctx) {
        Op result = visit(ctx.expr(0));
        visit(ctx.expr(1));
        if (ctx.multOp().STAR() != null) {
            emit(OpCode.mult, reg(ctx.expr(0)), reg(ctx.expr(1)), reg(ctx));
        } else {
            emit(OpCode.div, reg(ctx.expr(0)), reg(ctx.expr(1)), reg(ctx));
        }
        return result;
    }

    @Override
    public Op visitPlusExpr(SimplePascalParser.PlusExprContext ctx) {
        Op result = visit(ctx.expr(0));
        visit(ctx.expr(1));
        if (ctx.plusOp().PLUS() != null) {
            emit(OpCode.add, reg(ctx.expr(0)), reg(ctx.expr(1)), reg(ctx));
        } else {
            emit(OpCode.sub, reg(ctx.expr(0)), reg(ctx.expr(1)), reg(ctx));
        }
        return result;
    }

    @Override
    public Op visitCompExpr(SimplePascalParser.CompExprContext ctx) {
        Op result = visit(ctx.expr(0));
        visit(ctx.expr(1));

        if (ctx.compOp().LT() != null) {
            emit(OpCode.cmp_LT, reg(ctx.expr(0)), reg(ctx.expr(1)), reg(ctx));
        } else if (ctx.compOp().LE() != null) {
            emit(OpCode.cmp_LE, reg(ctx.expr(0)), reg(ctx.expr(1)), reg(ctx));
        } else if (ctx.compOp().EQ() != null) {
            emit(OpCode.cmp_EQ, reg(ctx.expr(0)), reg(ctx.expr(1)), reg(ctx));
        } else if (ctx.compOp().GE() != null) {
            emit(OpCode.cmp_GE, reg(ctx.expr(0)), reg(ctx.expr(1)), reg(ctx));
        } else if (ctx.compOp().GT() != null) {
            emit(OpCode.cmp_GT, reg(ctx.expr(0)), reg(ctx.expr(1)), reg(ctx));
        } else if (ctx.compOp().NE() != null) {
            emit(OpCode.cmp_NE, reg(ctx.expr(0)), reg(ctx.expr(1)), reg(ctx));
        }

        return result;
    }

    @Override
    public Op visitBoolExpr(SimplePascalParser.BoolExprContext ctx) {
        Op result = visit(ctx.expr(0));
        visit(ctx.expr(1));
        if (ctx.boolOp().AND() != null) {
            emit(OpCode.and, reg(ctx.expr(0)), reg(ctx.expr(1)), reg(ctx));
        } else {
            emit(OpCode.or, reg(ctx.expr(0)), reg(ctx.expr(1)), reg(ctx));
        }
        return result;
    }

    @Override
    public Op visitParExpr(SimplePascalParser.ParExprContext ctx) {
        Op result = visit(ctx.expr());
        regs.put(ctx, regs.get(ctx.expr()));
        return result;
    }

    @Override
    public Op visitIdExpr(SimplePascalParser.IdExprContext ctx) {
        return emit(OpCode.loadAI, arp, getOffset(ctx), reg(ctx));
    }

    @Override
    public Op visitNumExpr(SimplePascalParser.NumExprContext ctx) {
	    int val = Integer.parseInt(ctx.NUM().getSymbol().getText());
        return emit(OpCode.loadI, new Num(val), reg(ctx));
    }

    @Override
    public Op visitTrueExpr(SimplePascalParser.TrueExprContext ctx) {
        return emit(OpCode.loadI, new Num(1), reg(ctx));
    }

    @Override
    public Op visitFalseExpr(SimplePascalParser.FalseExprContext ctx) {
        return emit(OpCode.loadI, new Num(0), reg(ctx));
    }

    /** Constructs an operation from the parameters
	 * and adds it to the program under construction. */
	private Op emit(Label label, OpCode opCode, Operand... args) {
		Op result = new Op(label, opCode, args);
		this.prog.addInstr(result);
		return result;
	}

	/** Constructs an operation from the parameters 
	 * and adds it to the program under construction. */
	private Op emit(OpCode opCode, Operand... args) {
		return emit((Label) null, opCode, args);
	}

	/** 
	 * Looks up the label for a given parse tree node,
	 * creating it if none has been created before.
	 * The label is actually constructed from the entry node
	 * in the flow graph, as stored in the checker result.
	 */
	private Label label(ParserRuleContext node) {
		Label result = this.labels.get(node);
		if (result == null) {
			ParserRuleContext entry = this.checkResult.getEntry(node);
			result = createLabel(entry, "n");
			this.labels.put(node, result);
		}
		return result;
	}

	private Label getUniqueLabel(String name) {
	    labelCounter++;
	    return new Label(name + labelCounter);
    }

	/** Creates a label for a given parse tree node and prefix. */
	private Label createLabel(ParserRuleContext node, String prefix) {
		Token token = node.getStart();
		int line = token.getLine();
		int column = token.getCharPositionInLine();
		String result = prefix + "_" + line + "_" + column;
		return new Label(result);
	}

	/** Retrieves the getOffset of a variable node from the checker result,
	 * wrapped in a {@link Num} operand. */
	private Num getOffset(ParseTree node) {
		return new Num(this.checkResult.getOffset(node));
	}

	/** Returns a register for a given parse tree node,
	 * creating a fresh register if there is none for that node. */
	private Reg reg(ParseTree node) {
		Reg result = this.regs.get(node);
		if (result == null) {
			result = new Reg("r_" + this.regCount);
			this.regs.put(node, result);
			this.regCount++;
		}
		return result;
	}

	/** Assigns a register to a given parse tree node. */
	private void setReg(ParseTree node, Reg reg) {
		this.regs.put(node, reg);
	}
}
