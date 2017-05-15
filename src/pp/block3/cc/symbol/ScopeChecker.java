package pp.block3.cc.symbol;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wijtse on 12-5-2017.
 */
public class ScopeChecker extends DeclUseBaseListener {

    public static void main(String[] args) {
        new ScopeChecker().checkScope("declUseProg2.txt");
    }

    private SymbolTable symbolTable;
    private List<String> errorMessages;

    public ScopeChecker() {
        symbolTable = new MySymbolTable();
    }

    public List<String> checkScope(String fileName) {
        errorMessages = new ArrayList<>();

        // Convert the input text to a character stream
        CharStream stream = null;
        try {
            stream = CharStreams.fromFileName(fileName);
        } catch (IOException e) {
            System.out.println("Could not read file: " + fileName);
            System.exit(1);
        }
        // Build a lexer on top of the character stream
        Lexer lexer = new DeclUseLexer(stream);
        // Extract a token stream from the lexer
        TokenStream tokens = new CommonTokenStream(lexer);
        // Build a parser instance on top of the token stream
        DeclUseParser parser = new DeclUseParser(tokens);
        // Get the parse tree by calling the start rule
        ParseTree tree = parser.program();
        new ParseTreeWalker().walk(this, tree);

        System.out.println("Nr of msges: " + errorMessages.size());
        for (String msg : errorMessages) {
            System.err.println(msg);
        }
        return errorMessages;
    }

    @Override
    public void enterDecl(DeclUseParser.DeclContext ctx) {
        if (!symbolTable.add(ctx.ID().getText())) {
            errorMessages.add(ctx.getText() + " is already declared in the current scope! ("
                    + ctx.ID().getSymbol().getLine() + ":" + ctx.ID().getSymbol().getCharPositionInLine() + ")");
        }
    }

    @Override
    public void enterUse(DeclUseParser.UseContext ctx) {
        if (!symbolTable.contains(ctx.ID().getText())) {
            errorMessages.add(ctx.getText() + " is not yet defined! ("
                    + ctx.ID().getSymbol().getLine() + ":" + ctx.ID().getSymbol().getCharPositionInLine() + ")");
        }

    }

    @Override
    public void enterUnitSeries(DeclUseParser.UnitSeriesContext ctx) {
        symbolTable.openScope();
    }

    @Override
    public void exitUnitSeries(DeclUseParser.UnitSeriesContext ctx) {
        try {
            symbolTable.closeScope();
        } catch (RuntimeException e) {
            errorMessages.add("Can not close the global scope!");
        }
    }
}
