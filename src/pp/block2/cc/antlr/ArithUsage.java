package pp.block2.cc.antlr;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

public class ArithUsage {
	public static void main(String[] args) {
		parse("1+(2*3)^4");
		parse("(1^2)^3");
		parse("1----2)");
	}

	public static void parse(String text) {
		// Convert the input text to a character stream
		CharStream stream = CharStreams.fromString(text);
		// Build a lexer on top of the character stream
		Lexer lexer = new ArithLexer(stream);
		// Extract a token stream from the lexer
		TokenStream tokens = new CommonTokenStream(lexer);
		// Build a parser instance on top of the token stream
		ArithParser parser = new ArithParser(tokens);
		// Get the parse tree by calling the start rule
		ParseTree tree = parser.expr();
		// Print the (formatted) parse tree
		System.out.println(tree.toStringTree(parser));
	}
}
