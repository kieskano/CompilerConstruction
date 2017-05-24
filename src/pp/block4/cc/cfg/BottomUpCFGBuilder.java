package pp.block4.cc.cfg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import pp.block4.cc.ErrorListener;
import pp.block4.cc.cfg.FragmentParser.BlockStatContext;
import pp.block4.cc.cfg.FragmentParser.BreakStatContext;
import pp.block4.cc.cfg.FragmentParser.ContStatContext;
import pp.block4.cc.cfg.FragmentParser.DeclContext;
import pp.block4.cc.cfg.FragmentParser.IfStatContext;
import pp.block4.cc.cfg.FragmentParser.PrintStatContext;
import pp.block4.cc.cfg.FragmentParser.ProgramContext;
import pp.block4.cc.cfg.FragmentParser.WhileStatContext;

/** Template bottom-up CFG builder. */
public class BottomUpCFGBuilder extends FragmentBaseListener {
	/** The CFG being built. */
	private Graph graph;
	private Node startNode;

    private ParseTreeProperty<Node> enterNode;
    private ParseTreeProperty<Node> exitNode;

    public void init() {
        enterNode = new ParseTreeProperty<>();
        exitNode = new ParseTreeProperty<>();
    }

    /** Builds the CFG for a program contained in a given file. */
	public Graph build(File file) {
		Graph result = null;
		ErrorListener listener = new ErrorListener();
		try {
			CharStream chars = CharStreams.fromPath(file.toPath());
			Lexer lexer = new FragmentLexer(chars);
			lexer.removeErrorListeners();
			lexer.addErrorListener(listener);
			TokenStream tokens = new CommonTokenStream(lexer);
			FragmentParser parser = new FragmentParser(tokens);
			parser.removeErrorListeners();
			parser.addErrorListener(listener);
			ParseTree tree = parser.program();
			if (listener.hasErrors()) {
				System.out.printf("Parse errors in %s:%n", file.getPath());
				for (String error : listener.getErrors()) {
					System.err.println(error);
				}
			} else {
				result = build(tree);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/** Builds the CFG for a program given as an ANTLR parse tree. */
	public Graph build(ParseTree tree) {
	    init();
		this.graph = new Graph();
		startNode = graph.addNode("start");
		new ParseTreeWalker().walk(this, tree);
		return graph;
	}


    @Override
    public void exitProgram(ProgramContext ctx) {
        Node endNode = graph.addNode("end");
        startNode.addEdge(enterNode.get(ctx.stat(0)));
        for (int i = 0; i < ctx.stat().size() - 1; i++) {
            exitNode.get(ctx.stat(i)).addEdge(enterNode.get(ctx.stat(i+1)));
        }
        exitNode.get(ctx.stat(ctx.stat().size()-1)).addEdge(endNode);
    }

    @Override
    public void exitDecl(DeclContext ctx) {
        Node newNode = graph.addNode("decl");
        enterNode.put(ctx, newNode);
        exitNode.put(ctx, newNode);
    }

    @Override
    public void exitAssignStat(FragmentParser.AssignStatContext ctx) {
        Node newNode = graph.addNode("assign");
        enterNode.put(ctx, newNode);
        exitNode.put(ctx, newNode);
    }

    @Override
    public void exitPrintStat(PrintStatContext ctx) {
        Node newNode = graph.addNode("print");
        enterNode.put(ctx, newNode);
        exitNode.put(ctx, newNode);
    }

    @Override
    public void exitIfStat(IfStatContext ctx) {
        Node condition = graph.addNode("condition_if");
        Node afterIf = graph.addNode("after_if");
        enterNode.put(ctx, condition);

        exitNode.put(ctx, afterIf);

        for (FragmentParser.StatContext pn : ctx.stat()) {
            condition.addEdge(enterNode.get(pn));
            exitNode.get(pn).addEdge(afterIf);
        }
        if (ctx.stat().size() == 1) {
            condition.addEdge(afterIf);
        }
    }

    @Override
    public void exitWhileStat(WhileStatContext ctx) {
        Node condition = graph.addNode("condition_while");
        enterNode.put(ctx, condition);
        exitNode.put(ctx, condition);
        condition.addEdge(enterNode.get(ctx.stat()));
        exitNode.get(ctx.stat()).addEdge(condition);
    }

    @Override
    public void exitBlockStat(BlockStatContext ctx) {
        if (ctx.stat().size() == 0) {
            Node emptyNode = graph.addNode("empty_block");
            enterNode.put(ctx, emptyNode);
            exitNode.put(ctx, emptyNode);
        } else {
            Node startNode = enterNode.get(ctx.stat(0));
            for (int i = 0; i < ctx.stat().size() - 1; i++) {
                exitNode.get(ctx.stat(i)).addEdge(enterNode.get(ctx.stat(i+1)));
            }
            Node endNode = exitNode.get(ctx.stat(ctx.stat().size()-1));
            enterNode.put(ctx, startNode);
            exitNode.put(ctx, endNode);
        }
    }

    @Override
	public void enterBreakStat(BreakStatContext ctx) {
		throw new IllegalArgumentException("Break not supported");
	}

	@Override
	public void enterContStat(ContStatContext ctx) {
		throw new IllegalArgumentException("Continue not supported");
	}

	/** Adds a node to he CGF, based on a given parse tree node.
	 * Gives the CFG node a meaningful ID, consisting of line number and 
	 * a further indicator.
	 */
	private Node addNode(ParserRuleContext node, String text) {
		return this.graph.addNode(node.getStart().getLine() + ": " + text);
	}

	/** Main method to build and print the CFG of a simple Java program. */
	public static void main(String[] args) {
        List<String> files = new ArrayList<>();
        files.add("prog1.txt");
        files.add("prog2.txt");
        files.add("prog3.txt");
		BottomUpCFGBuilder builder = new BottomUpCFGBuilder();
		int i = 1;
		for (String filename : files) {
			File file = new File(filename);
			System.out.println(filename);
            Graph graph = builder.build(file);
			System.out.println(builder.build(file));
            try {
                graph.writeDOT("DOT-OUTPUT-" + i + ".dot", true);
            } catch (IOException e) {
                System.out.println("Could not write dot file for: " + filename);
            }
            i++;
        }
	}
}
