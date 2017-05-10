/**
 * 
 */
package pp.block2.cc.ll;

import pp.block2.cc.NonTerm;
import pp.block2.cc.SymbolFactory;
import pp.block2.cc.Term;

/**
 * Class containing some example grammars.
 * @author Arend Rensink
 *
 */
public class Grammars {
	/** Returns a grammar for simple English sentences. */
	public static Grammar makeSentence() {
		// Define the non-terminals
		NonTerm sent = new NonTerm("Sentence");
		NonTerm subj = new NonTerm("Subject");
		NonTerm obj = new NonTerm("Object");
		NonTerm mod = new NonTerm("Modifier");
		// Define the terminals, using the Sentence.g4 lexer grammar
		// Make sure you take the token constantss from the right class!
		SymbolFactory fact = new SymbolFactory(Sentence.class);
		Term noun = fact.getTerminal(Sentence.NOUN);
		Term verb = fact.getTerminal(Sentence.VERB);
		Term adj = fact.getTerminal(Sentence.ADJECTIVE);
		Term end = fact.getTerminal(Sentence.ENDMARK);
		// Build the context free grammar
		Grammar g = new Grammar(sent);
		g.addRule(sent, subj, verb, obj, end);
		g.addRule(subj, noun);
		g.addRule(subj, mod, subj);
		g.addRule(obj, noun);
		g.addRule(obj, mod, obj);
		g.addRule(mod, adj);
		return g;
	}

	public static Grammar makeIf() {
        // Define the non-terminals
        NonTerm statNT = new NonTerm("Stat");
        NonTerm elsePartNT = new NonTerm("ElsePart");
        // Define the terminals, using the If.g4 lexer grammar
        // Make sure you take the token constantss from the right class!
        SymbolFactory fact = new SymbolFactory(If.class);
        Term assign = fact.getTerminal(If.ASSIGN);
        Term cond = fact.getTerminal(If.COND);
        Term elseT = fact.getTerminal(If.ELSE);
        Term ifT = fact.getTerminal(If.IF);
        Term then = fact.getTerminal(If.THEN);
        // Build the context free grammar
        Grammar g = new Grammar(statNT);
        g.addRule(statNT, assign);
        g.addRule(statNT, ifT, cond, then, statNT, elsePartNT);
        g.addRule(elsePartNT, elseT, statNT);
        g.addRule(elsePartNT, Term.EMPTY);
        return g;
    }

    public static Grammar makeAba() {
        // Define the non-terminals
        NonTerm L = new NonTerm("L");
        NonTerm R = new NonTerm("R");
        NonTerm Ra = new NonTerm("Ra");
        NonTerm Q = new NonTerm("Q");
        NonTerm Qa = new NonTerm("Qa");
        // Define the terminals, using the Aba.g4 lexer grammar
        // Make sure you take the token constantss from the right class!
        SymbolFactory fact = new SymbolFactory(Aba.class);
        Term A = fact.getTerminal(Aba.A);
        Term B = fact.getTerminal(Aba.B);
        Term C = fact.getTerminal(Aba.C);
        // Build the context free grammar
        Grammar g = new Grammar(L);
        g.addRule(L, R, A);
        g.addRule(L, Q, B, A);
        g.addRule(R, A, B, A, Ra);
        g.addRule(R, C, A, B, A, Ra);
        g.addRule(Ra, B, C, Ra);
        g.addRule(Ra, Term.EMPTY);
        g.addRule(Q, B, Qa);
        g.addRule(Qa, B, C);
        g.addRule(Qa, C);
        return g;
    }
}
