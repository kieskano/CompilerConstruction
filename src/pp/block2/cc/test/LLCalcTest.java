package pp.block2.cc.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import pp.block2.cc.MyLLCalc;
import pp.block2.cc.NonTerm;
import pp.block2.cc.Symbol;
import pp.block2.cc.Term;
import pp.block2.cc.ll.*;

public class LLCalcTest {
	Grammar sentenceG = Grammars.makeSentence();
	// Define the non-terminals
	NonTerm subj = sentenceG.getNonterminal("Subject");
	NonTerm obj = sentenceG.getNonterminal("Object");
	NonTerm sent = sentenceG.getNonterminal("Sentence");
	NonTerm mod = sentenceG.getNonterminal("Modifier");
	// Define the terminals
	Term adj = sentenceG.getTerminal(Sentence.ADJECTIVE);
	Term noun = sentenceG.getTerminal(Sentence.NOUN);
	Term verb = sentenceG.getTerminal(Sentence.VERB);
	Term end = sentenceG.getTerminal(Sentence.ENDMARK);
	// Now add the last rule, causing the grammar to fail
	Grammar sentenceXG = Grammars.makeSentence();
	{    sentenceXG.addRule(mod, mod, mod);
	}
	LLCalc sentenceXLL = createCalc(sentenceXG);

	/** Tests the LL-calculator for the Sentence grammar. */
	@Test
	public void testSentenceOrigLL1() {
		// Without the last (recursive) rule, the grammar is LL-1
		assertTrue(createCalc(sentenceG).isLL1());
	}

	@Test
	public void testSentenceXFirst() {
		Map<Symbol, Set<Term>> first = sentenceXLL.getFirst();
		assertEquals(set(adj, noun), first.get(sent));
		assertEquals(set(adj, noun), first.get(subj));
		assertEquals(set(adj, noun), first.get(obj));
		assertEquals(set(adj), first.get(mod));
	}
	
	@Test
	public void testSentenceXFollow() {
		// FOLLOW sets
		Map<NonTerm, Set<Term>> follow = sentenceXLL.getFollow();
		assertEquals(set(Symbol.EOF), follow.get(sent));
		assertEquals(set(verb), follow.get(subj));
		assertEquals(set(end), follow.get(obj));
		assertEquals(set(noun, adj), follow.get(mod));
	}
	
	@Test
	public void testSentenceXFirstPlus() {
		// Test per rule
		Map<Rule, Set<Term>> firstp = sentenceXLL.getFirstp();
		List<Rule> subjRules = sentenceXG.getRules(subj);
		assertEquals(set(noun), firstp.get(subjRules.get(0)));
		assertEquals(set(adj), firstp.get(subjRules.get(1)));
	}
	
	@Test
	public void testSentenceXLL1() {
		assertFalse(sentenceXLL.isLL1());
	}

	/** Creates an LL1-calculator for a given grammar. */
	private LLCalc createCalc(Grammar g) {
		return new MyLLCalc(g);
	}

	@SuppressWarnings("unchecked")
	private <T> Set<T> set(T... elements) {
		return new HashSet<>(Arrays.asList(elements));
	}

	//=================================================================================================================
    //================                                  2CC4.2                                         ================
    //=================================================================================================================

	Grammar ifG = Grammars.makeIf(); // to be defined (Ex. 2-CC.4.1)
	// Define the non-terminals
	NonTerm stat = ifG.getNonterminal("Stat");
	NonTerm elsePart = ifG.getNonterminal("ElsePart");
	// Define the terminals (take from the right lexer grammar!)
    Term assign = ifG.getTerminal(If.ASSIGN);
    Term cond = ifG.getTerminal(If.COND);
    Term elseT = ifG.getTerminal(If.ELSE);
    Term ifT = ifG.getTerminal(If.IF);
    Term then = ifG.getTerminal(If.THEN);
	// (other terminals you need in the tests)
	Term eof = Symbol.EOF;
	Term empty = Symbol.EMPTY;
	LLCalc ifLL = createCalc(ifG);

    @Test
    public void testIfFirst() {
        Map<Symbol, Set <Term>> first = ifLL.getFirst();
        assertEquals(set(assign, ifT), first.get(stat));
        assertEquals(set(elseT, Term.EMPTY), first.get(elsePart));
    }
    @Test
    public void testIfFollow () {
        Map<NonTerm, Set<Term>> follow = ifLL.getFollow();
        assertEquals(set(Term.EOF, elseT), follow.get(stat));
        assertEquals(set(Term.EOF, elseT), follow.get(elsePart));
    }
    @Test
    public void testIfFirstPlus () {
        Map<Rule, Set <Term>> firstp = ifLL.getFirstp();
        List<Rule> statRules = ifG.getRules(stat);
        List<Rule> elseRules = ifG.getRules(elsePart);
        assertEquals(set(assign), firstp.get(statRules.get(0)));
        assertEquals(set(ifT), firstp.get(statRules.get(1)));
        assertEquals(set(elseT), firstp.get(elseRules.get(0)));
        assertEquals(set(elseT, Term.EOF, Term.EMPTY), firstp.get(elseRules.get(1)));
    }
    @Test
    public void testIfLL1() {
        assertFalse(ifLL.isLL1());
    }

    //=================================================================================================================
    //================                                  2CC4.2                                         ================
    //=================================================================================================================

    Grammar abaG = Grammars.makeAba(); // to be defined (Ex. 2-CC.4.1)
    // Define the non-terminals
    NonTerm L = abaG.getNonterminal("L");
    NonTerm R = abaG.getNonterminal("R");
    NonTerm Ra = abaG.getNonterminal("Ra");
    NonTerm Q = abaG.getNonterminal("Q");
    NonTerm Qa = abaG.getNonterminal("Qa");
    // Define the terminals (take from the right lexer grammar!)
    Term A = abaG.getTerminal(Aba.A);
    Term B = abaG.getTerminal(Aba.B);
    Term C = abaG.getTerminal(Aba.C);
    // (other terminals you need in the tests)
    LLCalc abaLL = createCalc(abaG);

    @Test
    public void testAbaFirst() {
        Map<Symbol, Set<Term>> first = abaLL.getFirst();
        assertEquals(set(A, B, C), first.get(L));
        assertEquals(set(A, C), first.get(R));
        assertEquals(set(B), first.get(Q));
        assertEquals(set(B, Term.EMPTY), first.get(Ra));
        assertEquals(set(B, C), first.get(Qa));
    }
    @Test
    public void testAbaFollow () {
        Map<NonTerm, Set<Term>> follow = abaLL.getFollow();
        assertEquals(set(Term.EOF), follow.get(L));
        assertEquals(set(A), follow.get(R));
        assertEquals(set(B), follow.get(Q));
        assertEquals(set(A), follow.get(Ra));
        assertEquals(set(B), follow.get(Qa));
    }
    @Test
    public void testAbaFirstPlus () {
        Map<Rule, Set <Term>> firstp = abaLL.getFirstp();
        List<Rule> LRules = abaG.getRules(L);
        List<Rule> RRules = abaG.getRules(R);
        List<Rule> RaRules = abaG.getRules(Ra);
        List<Rule> QRules = abaG.getRules(Q);
        List<Rule> QaRules = abaG.getRules(Qa);
        assertEquals(set(A,C), firstp.get(LRules.get(0)));
        assertEquals(set(B), firstp.get(LRules.get(1)));
        assertEquals(set(A), firstp.get(RRules.get(0)));
        assertEquals(set(C), firstp.get(RRules.get(1)));
        assertEquals(set(B), firstp.get(QRules.get(0)));
        assertEquals(set(B), firstp.get(RaRules.get(0)));
        assertEquals(set(Term.EMPTY, A), firstp.get(RaRules.get(1)));
        assertEquals(set(B), firstp.get(QaRules.get(0)));
        assertEquals(set(C), firstp.get(QaRules.get(1)));
    }
    @Test
    public void testAbaLL1() {
        assertTrue(abaLL.isLL1());
    }
}
