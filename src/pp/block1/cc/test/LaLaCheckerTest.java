package pp.block1.cc.test;

import org.junit.Assert;
import org.junit.Test;
import pp.block1.cc.dfa.Checker;
import pp.block1.cc.dfa.MyChecker;
import pp.block1.cc.dfa.State;

import static pp.block1.cc.dfa.State.DFA_LALA;
import static pp.block1.cc.dfa.State.ID6_DFA;

/** Test class for Checker implementation. */
public class LaLaCheckerTest {
	private Checker myChecker = new MyChecker(); // TODO instantiate your Checker implementation

	private State dfa;

	@Test
	public void testID6() {
		this.dfa = DFA_LALA;
		accepts("Laaa");
		rejects("");
		rejects("LaLi");
		rejects("Li");
		accepts("Laa__La");
		accepts("La_La_Laaaa_Li");
		rejects("La_La_Laaaa_La_Li");
	}


	private void accepts(String word) {
		if (!this.myChecker.accepts(this.dfa, word)) {
			Assert.fail(String.format(
					"Word '%s' is erroneously rejected by %s", word, this.dfa));
		}
	}

	private void rejects(String word) {
		if (this.myChecker.accepts(this.dfa, word)) {
			Assert.fail(String.format(
					"Word '%s' is erroneously accepted by %s", word, this.dfa));
		}
	}
}
