package pp.block1.cc.test;

import org.junit.Test;

import pp.block1.cc.antlr.Example;
import pp.block1.cc.antlr.Grammar1CC5;

public class Grammar1CC5Test {
    private static LexerTester tester = new LexerTester(Grammar1CC5.class);

    @Test
    public void succeedingTest() {
        tester.correct("a12345");
        tester.correct("a1d345a12345");
        tester.yields("a12cds a12b45b28482", Grammar1CC5.IDENTIFIER, Grammar1CC5.IDENTIFIER,
                Grammar1CC5.IDENTIFIER);
        tester.wrong("a12a123");
        tester.wrong("a12 567");
    }
}
