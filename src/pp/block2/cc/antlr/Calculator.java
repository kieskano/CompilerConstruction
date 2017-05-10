package pp.block2.cc.antlr;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.math.BigInteger;

import static pp.block2.cc.antlr.ArithParser.*;

/**
 * Created by Wijtse on 9-5-2017.
 */
public class Calculator extends ArithBaseVisitor {

    public BigInteger calculate(String expression) {
        // Convert the input text to a character stream
        CharStream stream = CharStreams.fromString(expression);
        // Build a lexer on top of the character stream
        Lexer lexer = new ArithLexer(stream);
        // Extract a token stream from the lexer
        TokenStream tokens = new CommonTokenStream(lexer);
        // Build a parser instance on top of the token stream
        ArithParser parser = new ArithParser(tokens);
        // Get the parse tree by calling the start rule
        ParseTree tree = parser.expr();

        return (BigInteger) visit(tree);
    }

    public enum OperatorType {
        PLUS, MIN, KEER, GED, MACHT;
    }

    @Override
    public Object visitExpr(ArithParser.ExprContext ctx) {
        switch (ctx.getChildCount()) {
            case 1:
                return visit(ctx.getChild(0));
            case 3:
                OperatorType opt = (OperatorType) visit(ctx.getChild(1));
                if (opt == OperatorType.PLUS) {
                    return ((BigInteger) visit(ctx.getChild(0))).add((BigInteger) visit(ctx.getChild(2)));
                } else {
                    return ((BigInteger) visit(ctx.getChild(0))).subtract((BigInteger) visit(ctx.getChild(2)));
                }
            default:
                System.err.println("VISIT EXPR ERROR");
                System.exit(1);
        }
        return super.visitExpr(ctx);
    }

    @Override
    public Object visitTerm(ArithParser.TermContext ctx) {
        switch (ctx.getChildCount()) {
            case 1:
                return visit(ctx.getChild(0));
            case 3:
                OperatorType opt = (OperatorType) visit(ctx.getChild(1));
                if (opt == OperatorType.KEER) {
                    return ((BigInteger) visit(ctx.getChild(0))).multiply((BigInteger) visit(ctx.getChild(2)));
                } else {
                    return ((BigInteger) visit(ctx.getChild(0))).divide((BigInteger) visit(ctx.getChild(2)));
                }
            default:
                System.err.println("VISIT TERM ERROR");
                System.exit(1);
        }
        return super.visitTerm(ctx);
    }

    @Override
    public Object visitFactor(ArithParser.FactorContext ctx) {
        switch (ctx.getChildCount()) {
            case 1:
                return visit(ctx.getChild(0));
            case 3:
                return ((BigInteger) visit(ctx.getChild(0))).pow(((BigInteger) visit(ctx.getChild(2))).intValue());
            default:
                System.err.println("VISIT FACTOR ERROR");
                System.exit(1);
        }
        return super.visitFactor(ctx);
    }

    @Override
    public Object visitPower(ArithParser.PowerContext ctx) {
        switch (ctx.getChildCount()) {
            case 1:
                return visit(ctx.getChild(0));
            case 3:
                return visit(ctx.getChild(1));
            default:
                System.err.println("VISIT POWER ERROR");
                System.exit(1);
        }
        return super.visitPower(ctx);
    }

    @Override
    public Object visitTerminal(TerminalNode node) {
        int type = node.getSymbol().getType();
        Object result = null;
        switch (type) {
            case OPP:
                if (node.getSymbol().getText().equals("+")) {
                    result = OperatorType.PLUS;
                } else {
                    result = OperatorType.MIN;
                }
                break;
            case OPM:
                if (node.getSymbol().getText().equals("*")) {
                    result = OperatorType.KEER;
                } else {
                    result = OperatorType.GED;
                }
                break;
            case POW:
                result = OperatorType.MACHT;
                break;
            case NR:
                int streepjes = 0;
                for (char chr : node.getSymbol().getText().toCharArray()) {
                    if (chr == '-') {
                        streepjes++;
                    } else {
                        break;
                    }
                }
                result = new BigInteger(node.getSymbol().getText().replace("-", ""))
                        .multiply(new BigInteger("-1").pow(streepjes));
                break;
            default:
                System.err.println("VISIT TERMINAL ERROR");
                System.exit(1);
        }
        return result;
    }
}
