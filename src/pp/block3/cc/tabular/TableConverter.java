package pp.block3.cc.tabular;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.w3c.dom.traversal.TreeWalker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Wijtse on 13-5-2017. 
 */
public class TableConverter extends TabularBaseListener {

    public static void main(String[] args) {
        new TableConverter().convert("% Another example to test the Tabular application.\n" +
                "\\begin{tabular}{rlrll}\n" +
                " 1 & ma & 14 & april & Introduction \\\\\n" +
                " 2 & wo & 16 & april & SyntaxAnalysis \\\\\n" +
                " 3 & ma & 21 & april & ContextualAnalysis \\\\\n" +
                " 4 & wo & 23 & april & ANTLR1 \\\\\n" +
                " 5 & wo &  7 & mei   & RunTime \\\\\n" +
                " 6 & wo & 14 & mei   & CodeGeneration \\\\\n" +
                " 7 & ma & 19 & mei   & CodeOptimization \\\\\n" +
                " 8 & wo & 21 & mei   & GarbageCollection \\\\\n" +
                " 9 & ma & 26 & mei   & ANTLR2 \\\\\n" +
                "\\end{tabular}\n" +
                "\n");
    }

    private String html;

    public void convert(String table) {
        html = "";
        // Convert the input text to a character stream
        CharStream stream = null;
        try {
            stream = CharStreams.fromFileName("src\\pp\\block3\\cc\\tabular\\tabular-3.tex");
        } catch (IOException e) {
            e.printStackTrace();
        }

//        CharStream stream = CharStreams.fromString(table);
        // Build a lexer on top of the character stream
        Lexer lexer = new TabularLexer(stream);
//        System.out.println(lexer.getAllTokens());
        // Extract a token stream from the lexer
        TokenStream tokens = new CommonTokenStream(lexer);
        // Build a parser instance on top of the token stream
        TabularParser parser = new TabularParser(tokens);
        MyErrorListener errorListener = new MyErrorListener();
        parser.removeErrorListeners(); // remove default reporting to stderr
        parser.addErrorListener(errorListener); // add your own error listener
        
        // Get the parse tree by calling the start rule
        ParseTree tree = parser.table();
        // Print the (formatted) parse tree
        System.out.println(tree.toStringTree(parser));
        if (!errorListener.heardErrors()) {
            new ParseTreeWalker().walk(this, tree);
            System.out.println(html);
            try {
                PrintWriter out = new PrintWriter("html_output.html");
                out.println(html);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void enterTable(TabularParser.TableContext ctx) {
        html += "<html>\n" +
                "<body>\n" +
                "<table border =\"1\">\n";
    }

    @Override
    public void exitTable(TabularParser.TableContext ctx) {
        html += "</table>\n" +
                "</body>\n" +
                "</html>";
    }

    @Override
    public void enterRowContent(TabularParser.RowContentContext ctx) {
        html += "<tr>\n" + "  <td>"
                + (ctx.ENTRY() != null ? ctx.ENTRY().getSymbol().getText() : "")
                + "</td>\n";
    }

    @Override
    public void exitRowContent(TabularParser.RowContentContext ctx) {
        html += "</tr>\n";
    }

    @Override
    public void enterRestRowEntry(TabularParser.RestRowEntryContext ctx) {
        html += "  <td>"
                + (ctx.ENTRY() != null ? ctx.ENTRY().getSymbol().getText() : "")
                + "</td>\n";
    }
}
