package pp.block1.cc.test;

import org.junit.Assert;
import org.junit.Test;
import pp.block1.cc.dfa.MyScanner;
import pp.block1.cc.dfa.Scanner;
import pp.block1.cc.dfa.State;

import java.util.List;

import static pp.block1.cc.dfa.State.DFA_LALA;
import static pp.block1.cc.dfa.State.ID6_DFA;

/** Test class for Scanner implementation. */
public class LaLaScannerTest {
	private Scanner myGen = new MyScanner(); // TODO instantiate your Scanner implementation

	@Test
	public void testID6() {
		this.dfa = DFA_LALA;
		yields("");
		yields("LaLa", "LaLa");
		yields("LaLaLa", "LaLa", "La");
		yields("LaLaLaLi", "LaLaLaLi");
//		yields("LaLaLaLaLi", "La", "LaLaLaLi");
		yields("LaLaLaLaLi", "LaLa", "LaLa");
		yields("Laaa_Laa_Laaa_Laaaa__Laa", "Laaa_Laa_", "Laaa_Laaaa__", "Laa");
	}

	private void yields(String word, String... tokens) {
		List<String> result = this.myGen.scan(this.dfa, word);
		if (result == null) {
			Assert.fail(String.format(
					"Word '%s' is erroneously rejected by %s", word, this.dfa));
		}
		Assert.assertEquals(tokens.length, result.size());
		for (int i = 0; i < tokens.length; i++) {
			Assert.assertEquals(tokens[i], result.get(i));
		}
	}

	private State dfa;
}
