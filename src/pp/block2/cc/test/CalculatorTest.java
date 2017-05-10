package pp.block2.cc.test;

import org.junit.Assert;
import org.junit.Test;
import pp.block2.cc.antlr.Calculator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CalculatorTest {

	@Test
	public void testCalculator() {
		Calculator calc = new Calculator();
        Assert.assertEquals(new BigInteger("512"), calc.calculate("2^3^2"));
        Assert.assertEquals(new BigInteger("-2"), calc.calculate("1+---3"));
        Assert.assertEquals(new BigInteger("4"), calc.calculate("1-(---3)"));
        Assert.assertEquals(new BigInteger("19"), calc.calculate("1+2*3^2"));
        Assert.assertEquals(new BigInteger("27"), calc.calculate("(1+2)*3^2"));
        Assert.assertEquals(new BigInteger("64"), calc.calculate("(2^3)^2"));
        try {
            Assert.assertEquals(new BigInteger(Files.readAllLines(Paths.get("VeryBigInteger.txt")).get(0)), calc.calculate(
                    "223^3^10 + 23412341+---769873 + 23451-(---36547) + " +
                            "178358790891+3516452*3584351321^12 + " +
                            "(231651+23564512)*3163^9 + (12862^5)^6"
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(new BigInteger("-1"), calc.calculate("---1"));
    }
}
