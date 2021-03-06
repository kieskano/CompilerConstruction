package pp.block5.cc.antlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import pp.block5.cc.ErrorListener;
import pp.block5.cc.ParseException;

/** Prettyprints a (number, word)-sentence and sums up the numbers. */
public class NumWordProcessor extends NumWordBaseVisitor<Integer> {
	public static void main(String[] args) {
		NumWordProcessor grouper = new NumWordProcessor();
		if (args.length == 0) {
			process(grouper, "1sock2shoes 3 holes");
			process(grouper, "3 strands 10 blocks 11 weeks 15 credits");
			process(grouper, "1 2 3");
		} else {
			for (String text : args) {
				process(grouper, text);
			}
		}
	}

	private static void process(NumWordProcessor grouper, String text) {
		try {
			System.out.printf("Processing '%s':%n", text);
			int result = grouper.group(text);
			System.out.println("Total: " + result);
		} catch (ParseException exc) {
			exc.print();
		}
	}

	private String string;

	/** Groups a given sentence and prints it to stdout.
	 * Returns the sum of the numbers in the sentence.
	 */
	public int group(String text) throws ParseException {
		CharStream chars = CharStreams.fromString(text);
		ErrorListener listener = new ErrorListener();
		Lexer lexer = new NumWordLexer(chars);
		lexer.removeErrorListeners();
		lexer.addErrorListener(listener);
		TokenStream tokens = new CommonTokenStream(lexer);
		NumWordParser parser = new NumWordParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(listener);
		ParseTree tree = parser.sentence();
		listener.throwException();
		return visit(tree);
	}

	// Override the visitor methods.
	// Each visitor method should call visit(child)
	// if and when it wants to visit that child node.


    @Override
    public Integer visitSentence(NumWordParser.SentenceContext ctx) {
        int result = 0;
        string = "";

        for (int i = 0; i < ctx.number().size()-2; i++) {
            result += visit(ctx.number(i));
            visit(ctx.word(i));
            string += ", ";
        }
        result += visit(ctx.number(ctx.number().size()-2));
        visit(ctx.word(ctx.number().size()-2));
        string += " and ";
        result += visit(ctx.number(ctx.number().size()-1));
        visit(ctx.word(ctx.number().size()-1));

        System.out.println(string);
        return result;
    }

    @Override
    public Integer visitNumber(NumWordParser.NumberContext ctx) {
        int number = Integer.parseInt(ctx.NUMBER().getSymbol().getText());
        string += number + " ";
        return number;
    }

    @Override
    public Integer visitWord(NumWordParser.WordContext ctx) {
        string += ctx.WORD();
	    return 0;
    }
}
