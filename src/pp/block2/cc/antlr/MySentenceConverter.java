package pp.block2.cc.antlr;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import pp.block2.cc.*;
import pp.block2.cc.ll.Sentence;

import java.util.HashMap;
import java.util.Map;

public class MySentenceConverter
		extends MySentenceBaseListener implements Parser {
	/** Factory needed to create terminals of the {@link Sentence}
	 * grammar. See {@link pp.block2.cc.ll.SentenceParser} for
	 * example usage. */
	private final SymbolFactory fact;

    private static final NonTerm SENT = new NonTerm("Sentence");
    private static final NonTerm SUBJ = new NonTerm("Subject");
    private static final NonTerm OBJ = new NonTerm("Object");
    private static final NonTerm MOD = new NonTerm("Modifier");

    /** Map from Antlr tree nodes to ASTs. */
    private Map<ParseTree, AST> nodeTreeMap;
    private boolean errorOccured = false;

	public MySentenceConverter() {
		this.fact = new SymbolFactory(Sentence.class);
	}

	@Override
	public AST parse(Lexer lexer) throws ParseException {
        nodeTreeMap = new HashMap<>();
	    SentenceParser parser = new SentenceParser(new CommonTokenStream(lexer));
		ParseTree tree = parser.sentence();
        new ParseTreeWalker().walk(this, tree);
        if (errorOccured) {
            throw new ParseException();
        }
		return convert(tree);
	}

	public AST convert(ParseTree node) {
	    AST result = nodeTreeMap.get(node);
	    for (int i = 0; i < node.getChildCount(); i++) {
	        result.addChild(convert(node.getChild(i)));
        }
        return result;
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        CommonToken token = (CommonToken) node.getSymbol();
        AST astNode = new AST(fact.getTerminal(token.getType()), token);
        nodeTreeMap.put(node, astNode);
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        CommonToken token = (CommonToken) node.getSymbol();
        AST astNode = new AST(fact.getTerminal(token.getType()), token);
        nodeTreeMap.put(node, astNode);
        errorOccured = true;
    }

    // From here on overwrite the listener methods
	// Use an appropriate ParseTreeProperty to
	// store the correspondence from nodes to ASTs
}
