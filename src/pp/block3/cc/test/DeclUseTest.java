package pp.block3.cc.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pp.block3.cc.symbol.ScopeChecker;

import java.util.List;

/**
 * Created by Wijtse on 13-5-2017.
 */
public class DeclUseTest {

    private ScopeChecker scopeChecker;

    @Before
    public void before() {
        scopeChecker = new ScopeChecker();
    }

    @Test
    public void test() {
        List<String> result = scopeChecker.checkScope("declUseProg1.txt");
        Assert.assertTrue(result.contains("D:noot is already declared in the current scope! (13:14)"));
    }

    @Test
    public void test2() {
        List<String> result = scopeChecker.checkScope("declUseProg2.txt");
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void test3() {
        List<String> result = scopeChecker.checkScope("declUseProg3.txt");
        Assert.assertTrue(result.contains("U:noot is not yet defined! (7:14)"));
    }

    @Test
    public void test4() {
        List<String> result = scopeChecker.checkScope("declUseProg4.txt");
        Assert.assertTrue(result.contains("U:noot is not yet defined! (7:14)"));
        Assert.assertTrue(result.contains("D:noot is already declared in the current scope! (12:14)"));
    }
}
