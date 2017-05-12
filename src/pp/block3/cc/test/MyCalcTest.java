package pp.block3.cc.test;

import static org.junit.Assert.assertEquals;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import pp.block3.cc.antlr.*;
import pp.block3.cc.antlr.MyCalcTypeParser.ExprContext;

public class MyCalcTest {
    private final ParseTreeWalker walker = new ParseTreeWalker();
    private final MyCalculator calc = new MyCalculator();

    @Test
    public void test() {
        test(Type.NUM, "2^3");
        test(Type.STR, "\"a\"^3");
        test(Type.ERR, "true^false");
        test(Type.NUM, "3+3");
        test(Type.BOOL, "true+false");
        test(Type.STR, "\"a\"+\"b\"");
        test(Type.ERR, "\"a\"+true");
        test(Type.BOOL, "3==3");
        test(Type.BOOL, "true==false");
        test(Type.BOOL, "\"a\"==\"b\"");
        test(Type.ERR, "3==\"a\"");


        test(Type.STR, "(\"a\"^3)^(2^3)");

    }

    private void test(Type expected, String expr) {
        assertEquals(expected, parseMyCalcType(expr).type);
        ParseTree tree = parseMyCalc(expr);
        this.calc.init();
        this.walker.walk(this.calc, tree);
        assertEquals(expected, this.calc.type(tree));
    }

    private ParseTree parseMyCalc(String text) {
        CharStream chars = CharStreams.fromString(text);
        Lexer lexer = new MyCalcLexer(chars);
        TokenStream tokens = new CommonTokenStream(lexer);
        MyCalcParser parser = new MyCalcParser(tokens);
        return parser.expr();
    }

    private ExprContext parseMyCalcType(String text) {
        CharStream chars = CharStreams.fromString(text);
        Lexer lexer = new MyCalcTypeLexer(chars);
        TokenStream tokens = new CommonTokenStream(lexer);
        MyCalcTypeParser parser = new MyCalcTypeParser(tokens);
        return parser.expr();
    }
}
