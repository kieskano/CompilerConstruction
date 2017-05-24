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
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import pp.block4.cc.ErrorListener;
import pp.block4.cc.cfg.FragmentParser.BreakStatContext;
import pp.block4.cc.cfg.FragmentParser.ContStatContext;
import pp.block4.cc.cfg.FragmentParser.DeclContext;
import pp.block4.cc.cfg.FragmentParser.IfStatContext;
import pp.block4.cc.cfg.FragmentParser.PrintStatContext;
import pp.block4.cc.cfg.FragmentParser.ProgramContext;

/** Template top-down CFG builder. */
public class TopDownCFGBuilder extends FragmentBaseListener {
	/** The CFG being built. */
	private Graph graph;

	private Node startNode;
	private Node endNode;
	private ParseTreeProperty<Node> assignedStartNode;
    private ParseTreeProperty<Node> assignedNextNode;

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
			ProgramContext tree = parser.program();
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
	public Graph build(ProgramContext tree) {
		this.graph = new Graph();
        startNode = graph.addNode("start");
        endNode = graph.addNode("end");
        assignedStartNode = new ParseTreeProperty<>();
        assignedNextNode = new ParseTreeProperty<>();
        new ParseTreeWalker().walk(this, tree);
		return graph;
	}

	@Override
	public void enterBreakStat(BreakStatContext ctx) {
		throw new IllegalArgumentException("Break not supported");
	}

	@Override
	public void enterContStat(ContStatContext ctx) {
		throw new IllegalArgumentException("Continue not supported");
	}

    @Override
    public void enterProgram(ProgramContext ctx) {
        Node dummyStartNode = graph.addNode("dubby");
	    for (int i = 0; i < ctx.stat().size()-1; i++) {
            FragmentParser.StatContext stat = ctx.stat(i);
	        Node dummyNextNode = graph.addNode("dubby");
	        assignedStartNode.put(stat, dummyStartNode);
            assignedNextNode.put(stat, dummyNextNode);
            dummyStartNode = dummyNextNode;
        }
        startNode.addEdge(assignedStartNode.get(ctx.stat(0)));

        assignedStartNode.put(ctx.stat(ctx.stat().size()-1), dummyStartNode);
	    assignedNextNode.put(ctx.stat(ctx.stat().size()-1), endNode);
    }

    @Override
    public void enterDecl(DeclContext ctx) {
        assignedStartNode.get(ctx).setId("decl");
        assignedStartNode.get(ctx).addEdge(assignedNextNode.get(ctx));
    }

    @Override
    public void enterAssignStat(FragmentParser.AssignStatContext ctx) {
        assignedStartNode.get(ctx).setId("assign");
        assignedStartNode.get(ctx).addEdge(assignedNextNode.get(ctx));
    }

    @Override
    public void enterIfStat(IfStatContext ctx) {
        assignedStartNode.get(ctx).setId("condition_if");
        Node dummyNode = graph.addNode("dubby");

        Node afterIf = graph.addNode("after_if");
        assignedStartNode.get(ctx).addEdge(dummyNode);
        assignedStartNode.put(ctx.stat(0), dummyNode);
        assignedNextNode.put(ctx.stat(0), afterIf);

        if (ctx.stat().size() == 1) {
            assignedStartNode.get(ctx).addEdge(afterIf);
        } else {
            dummyNode = graph.addNode("dubby");
            assignedStartNode.get(ctx).addEdge(dummyNode);
            assignedStartNode.put(ctx.stat(1), dummyNode);
            assignedNextNode.put(ctx.stat(1), afterIf);
        }
        afterIf.addEdge(assignedNextNode.get(ctx));
    }

    @Override
    public void enterWhileStat(FragmentParser.WhileStatContext ctx) {
        assignedStartNode.get(ctx).setId("condition_while");
        Node dummyNode = graph.addNode("dubby");
        assignedStartNode.get(ctx).addEdge(assignedNextNode.get(ctx));
        assignedStartNode.get(ctx).addEdge(dummyNode);
        assignedStartNode.put(ctx.stat(), dummyNode);
        assignedNextNode.put(ctx.stat(), assignedStartNode.get(ctx));
    }

    @Override
    public void enterBlockStat(FragmentParser.BlockStatContext ctx) {
        Node dummyStartNode = assignedStartNode.get(ctx);
        for (int i = 0; i < ctx.stat().size()-1; i++) {
            FragmentParser.StatContext stat = ctx.stat(i);
            Node dummyNextNode = graph.addNode("dubby");
            assignedStartNode.put(stat, dummyStartNode);
            assignedNextNode.put(stat, dummyNextNode);
            dummyStartNode = dummyNextNode;
        }
        if (ctx.stat().size() > 1) {
            assignedStartNode.put(ctx.stat(ctx.stat().size() - 1), dummyStartNode);
            assignedNextNode.put(ctx.stat(ctx.stat().size() - 1), assignedNextNode.get(ctx));
        } else {
            assignedStartNode.put(ctx.stat(0), dummyStartNode);
            assignedNextNode.put(ctx.stat(0), assignedNextNode.get(ctx));
        }
    }

    @Override
    public void enterPrintStat(PrintStatContext ctx) {
        assignedStartNode.get(ctx).setId("print");
        assignedStartNode.get(ctx).addEdge(assignedNextNode.get(ctx));
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
        TopDownCFGBuilder builder = new TopDownCFGBuilder();
        int i = 1;
        for (String filename : files) {
            File file = new File(filename);
            System.out.println(filename);
            Graph graph = builder.build(file);
            System.out.println(graph);
            try {
                graph.writeDOT("DOT-OUTPUT-" + i + ".dot", true);
            } catch (IOException e) {
                System.out.println("Could not write dot file for: " + filename);
            }
            i++;
        }
	}
}
