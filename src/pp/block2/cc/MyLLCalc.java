package pp.block2.cc;

import pp.block2.cc.ll.Grammar;
import pp.block2.cc.ll.LLCalc;
import pp.block2.cc.ll.Rule;

import java.util.*;

/**
 * Created by Wijtse on 8-5-2017.
 */
public class MyLLCalc implements LLCalc {

    private Grammar grammar;
    private Map<Symbol, Set<Term>> first;
    private Map<NonTerm, Set<Term>> follow;
    private Map<Rule, Set<Term>> firstp;

    public MyLLCalc(Grammar grammar) {
        this.grammar = grammar;
    }

    @Override
    public Map<Symbol, Set<Term>> getFirst() {
        if (first == null) {
            first = new HashMap<>();
            for (Symbol symbol : grammar.getNonterminals()) {
                first.put(symbol, new HashSet<>());
            }
            for (Term symbol : grammar.getTerminals()) {
                Set set = new HashSet<>();
                set.add(symbol);
                first.put(symbol, set);
            }

            boolean somethingChanged = true;
            while (somethingChanged) {
                somethingChanged = false;
                for (NonTerm nonTerm : grammar.getNonterminals()) {
                    for (Rule rule : grammar.getRules(nonTerm)) {
                        Set<Term> FIRSTOfNonTerm = first.get(nonTerm);
                        for (Symbol rSymbol : rule.getRHS()) {
                            Set<Term> FIRSTOfRSymbol = first.get(rSymbol);
                            boolean containsEmpty = FIRSTOfRSymbol.contains(Term.EMPTY);

                            if (containsEmpty && FIRSTOfRSymbol.size() > 1) {
                                Set<Term> addSet = new HashSet<>();
                                for (Term term : FIRSTOfRSymbol) {
                                    if (!term.equals(Term.EMPTY)) {
                                        addSet.add(term);
                                    }
                                }
                                somethingChanged = somethingChanged || FIRSTOfNonTerm.addAll(addSet);
                            } else {
                                somethingChanged = somethingChanged || FIRSTOfNonTerm.addAll(FIRSTOfRSymbol);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return first;
    }

    @Override
    public Map<NonTerm, Set<Term>> getFollow() {
        if (follow == null) {
            follow = new HashMap<>();

            Set<Term> FOLLOWStart = new HashSet<>();
            FOLLOWStart.add(Term.EOF);
            follow.put(grammar.getStart(), FOLLOWStart);

            for (NonTerm nonTerm : grammar.getNonterminals()) {
                if (!nonTerm.equals(grammar.getStart())) {
                    Set<Term> FOLLOWInit = new HashSet<>();
                    follow.put(nonTerm, FOLLOWInit);
                }
            }

            boolean somethingChanged = true;
            while (somethingChanged) {
                somethingChanged = false;
                for (NonTerm nonTerm : grammar.getNonterminals()) {
                    for (Rule rule : grammar.getRules(nonTerm)) {

                        Set<Term> TRAILER = new HashSet<>();
                        TRAILER.addAll(follow.get(rule.getLHS()));

                        for (int i = rule.getRHS().size() - 1; i >= 0; i--) {
                            if (rule.getRHS().get(i) instanceof  NonTerm) {
                                somethingChanged = somethingChanged || follow.get(rule.getRHS().get(i)).addAll(TRAILER);
                                if (getFirst().get(rule.getRHS().get(i)).contains(Term.EMPTY)) {
                                    TRAILER.addAll(getFirst().get(rule.getRHS().get(i)));
                                    TRAILER.remove(Term.EMPTY);
                                } else {
                                    TRAILER = new HashSet<>();
                                    TRAILER.addAll(getFirst().get(rule.getRHS().get(i)));
                                }
                            } else {
                                TRAILER = new HashSet<>();
                                TRAILER.addAll(getFirst().get(rule.getRHS().get(i)));
                            }
                        }
                    }
                }
            }
        }
        return follow;
    }

    @Override
    public Map<Rule, Set<Term>> getFirstp() {
        if (firstp == null) {
            firstp = new HashMap<>();
            for (Rule rule : grammar.getRules()) {

                Set<Term> FIRSTpOfRule = new HashSet<>();
                FIRSTpOfRule.addAll(getFirst().get(rule.getRHS().get(0)));

                if (FIRSTpOfRule.contains(Term.EMPTY)) {
                    FIRSTpOfRule.addAll(getFollow().get(rule.getLHS()));
                }

                firstp.put(rule, FIRSTpOfRule);
            }
        }
        return firstp;
    }

    @Override
    public boolean isLL1() {
        boolean result = true;
        for (NonTerm nonTerm : grammar.getNonterminals()) {
            List<Rule> rules = grammar.getRules(nonTerm);
            for (int i = 0; i < rules.size(); i++) {
                for (int j = i + 1; j < rules.size(); j++) {
                    result = result && unionIsEmpty(getFirstp().get(rules.get(i)), getFirstp().get(rules.get(j)));
                    if (!result) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    public boolean unionIsEmpty(Set<Term> a, Set<Term> b) {
        int expectedSize = a.size() + b.size();
        a.addAll(b);
        return expectedSize == a.size();
    }
}
