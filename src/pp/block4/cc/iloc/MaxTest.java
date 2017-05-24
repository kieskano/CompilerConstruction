package pp.block4.cc.iloc;

import org.junit.Before;
import org.junit.Test;
import pp.iloc.Assembler;
import pp.iloc.Simulator;
import pp.iloc.eval.Machine;
import pp.iloc.model.Program;
import pp.iloc.parse.FormatException;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by han on 24-5-17.
 */
public class MaxTest {

    @Test
    public void testAssember() {
        //GET PROGRAM
        Program program = parse("max");
        String print = program.prettyPrint();
        if (SHOW) {
            System.out.println("Program :");
            System.out.print(print);
        }
        //COMPARE WIHT PRINTPRETTY
        Program other = null;
        try {
            other = Assembler.instance().assemble(print);
        } catch (FormatException e) {
            fail(e.getMessage());
        }
        assertEquals(program, other);
    }

    @Test(timeout = 1000)
    public void testSimulator() {
        Program program = parse("max");
        Machine machine = new Machine();
        int max = machine.init("a", 1,2,3,4,5,6,7,8,9,98,87,76,65,54,43,32,21);
        //int len = machine.init("alength", 17);
        machine.setNum("alength", 17);
        System.out.println(machine);
        new Simulator(program, machine).run();
        if (SHOW) {
            System.out.println(machine);
        }
        assertEquals(98, machine.getReg("r_max"));
    }

    public Program parse(String filename) {
        //READ FILE
        File file = new File(filename + ".iloc");
        if (!file.exists()) {
            file = new File(BASE_DIR + filename + ".iloc");
        }
        //ASSEMBLE
        try {
            Program result = Assembler.instance().assemble(file);
            return result;
        } catch (FormatException | IOException e) {
            fail(e.getMessage());
            return null;
        }
    }

    public static final boolean SHOW = true;
    public static final String BASE_DIR = "src/pp/block4/cc/iloc/";

}

/*

4-CC.6.3

The size of an int in memory is four bytes, and the index r_i is incremented by one every cycle so to get the right off-
set from @a (the constant that indicates memory location of the array a) in r_a you need to multiply the index by four
to get the i-th element.


 */
