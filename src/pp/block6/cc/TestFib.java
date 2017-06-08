package pp.block6.cc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import org.junit.Test;

import pp.iloc.Assembler;
import pp.iloc.Simulator;
import pp.iloc.eval.Machine;
import pp.iloc.model.Program;
import pp.iloc.parse.FormatException;

@SuppressWarnings("javadoc")
public class TestFib {
    private Assembler assembler = Assembler.instance();
    private final static boolean SHOW = true;

    @Test
    //(timeout = 1000)
    public void simulate() {
        Program p = assemble("src/pp/block6/cc/fib");
        if (SHOW) {
            //System.out.println(p.prettyPrint());
        }
        Machine vm = new Machine();
        Simulator sim = new Simulator(p, vm);
        sim.setIn(new ByteArrayInputStream(("" + 40).getBytes()));
        sim.run();
        if (SHOW) {
            System.out.println(vm);
        }
    }



    private Program assemble(String filename) {
        File file = new File(filename + ".iloc");
        try {
            return this.assembler.assemble(file);
        } catch (FormatException | IOException e) {
            System.out.println(e);
            fail(e.getMessage());
            return null;
        }
    }
}
