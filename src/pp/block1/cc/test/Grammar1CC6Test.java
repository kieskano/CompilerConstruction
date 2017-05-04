package pp.block1.cc.test;

import org.junit.Test;

import pp.block1.cc.antlr.Example;
import pp.block1.cc.antlr.Grammar1CC5;
import pp.block1.cc.antlr.Grammar1CC6;

public class Grammar1CC6Test {
    private static LexerTester tester = new LexerTester(Grammar1CC6.class);

    @Test
    public void succeedingTest() {
        tester.correct("\"Hoi ik \"\"ben\"\" Jaap\"");
        tester.correct("\"Hoi ik \"\"ben\"\" Jaap \"\"Joop\"\"\"");
        tester.yields("\"Hoi ik \"\"ben\"\" Jaap\"", Grammar1CC6.PL1STRING);
        tester.yields("\"Hoi ik \"\"ben\"\" Jaap\" \"Hoi ik \"\"ben\"\" Jaap\"", Grammar1CC6.PL1STRING,
                Grammar1CC6.PL1STRING);
        tester.yields("\"Hoi ik \" \"ben\"\" Jaap\" \"Hoi ik \"\"ben\"\" Jaap\"", Grammar1CC6.PL1STRING,
                Grammar1CC6.PL1STRING, Grammar1CC6.PL1STRING);
        tester.wrong("\"Hoi ik \" ben\" Jaap\"");
    }
}
