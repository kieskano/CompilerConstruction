//package pp.block1.cc.test;
//
//import org.junit.Test;
//import pp.block1.cc.antlr.Grammar1CC7;
//
//public class Grammar1CC7Test {
//    private static LexerTester tester = new LexerTester(Grammar1CC7.class);
//
//
//    @Test
//    public void succeedingTest() {
//        tester.correct("LaLa");
//        tester.correct("Laaaaa____");
//        tester.correct("Laaaaa____Laa");
//        tester.correct("Laaaaa____LaaLa_Li");
//        tester.correct("Laaaa Laaaa La");
//        tester.wrong("Li");
//        tester.wrong("____La");
//        tester.wrong("LaLaLi");
//        tester.wrong("La LaLaLi");
//        tester.yields("LaLaLaLi", Grammar1CC7.LALALALI);
//        tester.yields("Laaaaa____", Grammar1CC7.LA);
//        tester.yields("Laaa__Laa_La", Grammar1CC7.LALA, Grammar1CC7.LA);
//        tester.yields("LaLaLaLi", Grammar1CC7.LALALALI);
//        tester.yields("LaLa LaLa", Grammar1CC7.LALA, Grammar1CC7.LALA);
//    }
//}
