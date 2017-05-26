package pp.block4.cc.iloc;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import pp.block4.cc.ErrorListener;
import pp.block4.cc.iloc.CalcParser.CompleteContext;
import pp.iloc.Simulator;
import pp.iloc.model.*;

/** Compiler from Calc.g4 to ILOC. */
public class CalcCompiler extends CalcBaseListener {
	/** Program under construction. */
	private Program prog;
	// Attribute maps and other fields

    private ParseTreeProperty<Reg> register;
    private int curRegNr;

	/** Compiles a given expression string into an ILOC program. */
	public Program compile(String text) {
		Program result = null;
		ErrorListener listener = new ErrorListener();
		CharStream chars = CharStreams.fromString(text);
		Lexer lexer = new CalcLexer(chars);
		lexer.removeErrorListeners();
		lexer.addErrorListener(listener);
		TokenStream tokens = new CommonTokenStream(lexer);
		CalcParser parser = new CalcParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(listener);
		ParseTree tree = parser.complete();
		if (listener.hasErrors()) {
			System.out.printf("Parse errors in %s:%n", text);
			for (String error : listener.getErrors()) {
				System.err.println(error);
			}
		} else {
			result = compile(tree);
		}
		return result;
	}

	/** Compiles a given Calc-parse tree into an ILOC program. */
	public Program compile(ParseTree tree) {
		prog = new Program();
        register = new ParseTreeProperty<>();
        curRegNr = 0;
        new ParseTreeWalker().walk(this, tree);
        return prog;
	}

    @Override
    public void exitComplete(CompleteContext ctx) {
        emit(OpCode.out, new Str("Outcome: "), register.get(ctx.expr()));
    }

    @Override
    public void exitPar(CalcParser.ParContext ctx) {
        register.put(ctx, register.get(ctx.expr()));
    }

    @Override
    public void exitMinus(CalcParser.MinusContext ctx) {
        Reg reg = getNextRegister();
        register.put(ctx, reg);
        emit(OpCode.rsubI, register.get(ctx.expr()), new Num(0), reg);
    }

    @Override
    public void exitNumber(CalcParser.NumberContext ctx) {
	    int value = Integer.parseInt(ctx.NUMBER().getSymbol().getText());
	    Reg reg = getNextRegister();
	    register.put(ctx, reg);
        emit(OpCode.loadI, new Num(value), reg);
    }

    @Override
    public void exitTimes(CalcParser.TimesContext ctx) {
        Reg reg = getNextRegister();
        register.put(ctx, reg);
        emit(OpCode.mult, register.get(ctx.expr(0)), register.get(ctx.expr(1)), reg);
    }

    @Override
    public void exitPlus(CalcParser.PlusContext ctx) {
        Reg reg = getNextRegister();
        register.put(ctx, reg);
        emit(OpCode.add, register.get(ctx.expr(0)), register.get(ctx.expr(1)), reg);
    }

    public Reg getNextRegister() {
	    Reg result = new Reg("r_" + curRegNr);
	    curRegNr++;
	    return result;
    }

    /** Constructs an operation from the parameters
	 * and adds it to the program under construction. */
	private void emit(OpCode opCode, Operand... args) {
		this.prog.addInstr(new Op(opCode, args));
	}

	/** Calls the compiler, and simulates and prints the compiled program. */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Usage: [expr]+");
			return;
		}
		CalcCompiler compiler = new CalcCompiler();
		for (String expr : args) {
			System.out.println("Processing " + expr);
			Program prog = compiler.compile(expr);
			new Simulator(prog).run();
			System.out.println(prog.prettyPrint());
		}
	}
}
