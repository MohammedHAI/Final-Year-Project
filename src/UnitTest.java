/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 04/04/2024
 */

import components.OutputBuffer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnitTest {
    @Test
    // memory test
    public void testGui1() { // test setting up memory
        short[] memory = new short[] {0, 1, 2};
        //OutputBuffer buffer = new OutputBuffer();
        //VirtualComputer vc = new VirtualComputer(1, buffer);
        BaseWindow bw = new BaseWindow(memory);
        //MainController.main(vc, bw, buffer);

        assertEquals(String.valueOf(memory[0]), bw.viewer.table.getModel().getValueAt(0, 1));
        assertEquals(String.valueOf(memory[1]), bw.viewer.table.getModel().getValueAt(0, 2));
        assertEquals(String.valueOf(memory[2]), bw.viewer.table.getModel().getValueAt(0, 3));
    }

    @Test
    // code editor test
    public void testGui2() {
        short[] memory = new short[] {0, 1, 2};
        BaseWindow bw = new BaseWindow(memory);

        assertTrue(bw.editor.textArea.getText().isEmpty());
        bw.editor.textArea.setText(".main");
        assertFalse(bw.editor.textArea.getText().isEmpty());
    }

    @Test
    // registers test
    public void testGui3() {
        short[] memory = new short[] {0, 1, 2};
        BaseWindow bw = new BaseWindow(memory);

        bw.registers.field1.setText("10");
        assertEquals("10", bw.registers.field1.getText());
    }

    @Test
    // output test
    public void testGui4() {
        short[] memory = new short[] {0, 1, 2};
        BaseWindow bw = new BaseWindow(memory);

        assertTrue(bw.output.textArea.getText().isEmpty());
        bw.output.textArea.setText("Hello, world.");
        assertFalse(bw.output.textArea.getText().isEmpty());
    }

    // -- Virtual Computer --

    @Before
    public void setupInstructionLookups() {
        Instruction.setupLookups();
    }

    @Test
    public void testInstruction1() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(0, Instruction.compileOpcode("NOP"));
        vc.state.mm.write(1, (short) 0);
        vc.step();
        assertEquals(2, vc.state.PC);
    }

    @Test
    public void testInstruction2() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(0, Instruction.compileOpcode("LDIA"));
        vc.state.mm.write(1, (short) 1);
        vc.step();
        assertEquals(1, vc.state.registers[0].read());
    }

    @Test
    public void testInstruction3() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(0, Instruction.compileOpcode("LDIB"));
        vc.state.mm.write(1, (short) 1);
        vc.step();
        assertEquals(1, vc.state.registers[1].read());
    }

    @Test
    public void testInstruction4() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(0, Instruction.compileOpcode("LDIC"));
        vc.state.mm.write(1, (short) 1);
        vc.step();
        assertEquals(1, vc.state.registers[2].read());
    }

    @Test
    public void testInstruction5() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(0, Instruction.compileOpcode("LDID"));
        vc.state.mm.write(1, (short) 1);
        vc.step();
        assertEquals(1, vc.state.registers[3].read());
    }

    @Test
    public void testInstruction6() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(2, (short) 1);
        vc.state.mm.write(0, Instruction.compileOpcode("LDAA"));
        vc.state.mm.write(1, (short) 2);
        vc.step();
        assertEquals(1, vc.state.registers[0].read());
    }

    @Test
    public void testInstruction7() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(2, (short) 1);
        vc.state.mm.write(0, Instruction.compileOpcode("LDAB"));
        vc.state.mm.write(1, (short) 2);
        vc.step();
        assertEquals(1, vc.state.registers[1].read());
    }

    @Test
    public void testInstruction8() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(2, (short) 1);
        vc.state.mm.write(0, Instruction.compileOpcode("LDAC"));
        vc.state.mm.write(1, (short) 2);
        vc.step();
        assertEquals(1, vc.state.registers[2].read());
    }

    @Test
    public void testInstruction9() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(2, (short) 1);
        vc.state.mm.write(0, Instruction.compileOpcode("LDAD"));
        vc.state.mm.write(1, (short) 2);
        vc.step();
        assertEquals(1, vc.state.registers[3].read());
    }

    @Test
    public void testInstruction10() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(0, Instruction.compileOpcode("STRA"));
        vc.state.mm.write(1, (short) 0);
        vc.step();
        assertEquals(0, vc.state.mm.read(0));
    }

    @Test
    public void testInstruction11() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(0, Instruction.compileOpcode("STRB"));
        vc.state.mm.write(1, (short) 0);
        vc.step();
        assertEquals(0, vc.state.mm.read(0));
    }

    @Test
    public void testInstruction12() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(0, Instruction.compileOpcode("STRC"));
        vc.state.mm.write(1, (short) 0);
        vc.step();
        assertEquals(0, vc.state.mm.read(0));
    }

    @Test
    public void testInstruction13() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(0, Instruction.compileOpcode("STRD"));
        vc.state.mm.write(1, (short) 0);
        vc.step();
        assertEquals(0, vc.state.mm.read(0));
    }

    @Test
    public void testInstruction14() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(2, (short) 1);
        vc.state.mm.write(0, Instruction.compileOpcode("STAA"));
        vc.state.mm.write(1, (short) 2);
        vc.step();
        assertEquals(0, vc.state.mm.read(2));
    }

    @Test
    public void testInstruction15() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(2, (short) 1);
        vc.state.mm.write(0, Instruction.compileOpcode("STAB"));
        vc.state.mm.write(1, (short) 2);
        vc.step();
        assertEquals(0, vc.state.mm.read(2));
    }

    @Test
    public void testInstruction16() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(2, (short) 1);
        vc.state.mm.write(0, Instruction.compileOpcode("STAC"));
        vc.state.mm.write(1, (short) 2);
        vc.step();
        assertEquals(0, vc.state.mm.read(2));
    }

    @Test
    public void testInstruction17() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(2, (short) 1);
        vc.state.mm.write(0, Instruction.compileOpcode("STAD"));
        vc.state.mm.write(1, (short) 2);
        vc.step();
        assertEquals(0, vc.state.mm.read(2));
    }

    @Test
    public void testInstruction18() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.registers[0].write((short) 5);
        vc.state.mm.write(0, Instruction.compileOpcode("MOV"));
        vc.state.mm.write(1, (short) 4);
        vc.step();
        assertEquals(5, vc.state.registers[1].read());
    }

    @Test
    public void testInstruction19() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.registers[0].write((short) 1);
        vc.state.mm.write(0, Instruction.compileOpcode("ADD"));
        vc.state.mm.write(1, (short) 0);
        vc.step();
        assertEquals(2, vc.state.registers[0].read());
    }

    @Test
    public void testInstruction20() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.registers[0].write((short) 1);
        vc.state.mm.write(0, Instruction.compileOpcode("SUB"));
        vc.state.mm.write(1, (short) 0);
        vc.step();
        assertEquals(0, vc.state.registers[0].read());
    }

    @Test
    public void testInstruction21() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(0, Instruction.compileOpcode("JMP"));
        vc.state.mm.write(1, (short) 4);
        vc.step();
        assertEquals(4, vc.state.PC);
        assertEquals(1, vc.state.SP);
    }

    @Test
    public void testInstruction22() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(0, Instruction.compileOpcode("CMPE"));
        vc.state.mm.write(1, (short) 0);
        vc.step();
        assertEquals(0b00000100, vc.state.statusFlag & 0b00000100); // Status.EQUAL
    }

    @Test
    public void testInstruction23() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.registers[1].write((short) 1);
        vc.state.mm.write(0, Instruction.compileOpcode("CMPG"));
        vc.state.mm.write(1, (short) 4);
        vc.step();
        assertEquals(0b0000010, vc.state.statusFlag & 0b00000010); // Status.GREATER
    }

    @Test
    public void testInstruction24() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(0, Instruction.compileOpcode("BRC"));
        vc.state.mm.write(1, (short) 4);
        vc.step();
        assertEquals(4, vc.state.PC);
    }

    @Test
    public void testInstruction25() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(0, Instruction.compileOpcode("CMPE"));
        vc.state.mm.write(1, (short) 0);
        vc.state.mm.write(2, Instruction.compileOpcode("BEQ"));
        vc.state.mm.write(3, (short) 6);
        vc.step();
        vc.step();
        assertEquals(6, vc.state.PC);
    }

    @Test
    public void testInstruction26() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.registers[1].write((short) 1);
        vc.state.mm.write(0, Instruction.compileOpcode("CMPE"));
        vc.state.mm.write(1, (short) 1);
        vc.state.mm.write(2, Instruction.compileOpcode("BNE"));
        vc.state.mm.write(3, (short) 6);
        vc.step();
        vc.step();
        assertEquals(6, vc.state.PC);
    }

    @Test
    public void testInstruction27() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.registers[1].write((short) 1);
        vc.state.mm.write(0, Instruction.compileOpcode("CMPG"));
        vc.state.mm.write(1, (short) 4);
        vc.state.mm.write(2, Instruction.compileOpcode("BGT"));
        vc.state.mm.write(3, (short) 6);
        vc.step();
        vc.step();
        assertEquals(6, vc.state.PC);
    }

    @Test
    public void testInstruction28() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.registers[1].write((short) 1);
        vc.state.mm.write(0, Instruction.compileOpcode("CMPG"));
        vc.state.mm.write(1, (short) 1);
        vc.state.mm.write(2, Instruction.compileOpcode("BLT"));
        vc.state.mm.write(3, (short) 6);
        vc.step();
        vc.step();
        assertEquals(6, vc.state.PC);
    }

    @Test
    public void testInstruction29() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.registers[1].write((short) 1);
        vc.state.mm.write(0, Instruction.compileOpcode("CMPG"));
        vc.state.mm.write(1, (short) 1);
        vc.state.mm.write(2, Instruction.compileOpcode("BLT"));
        vc.state.mm.write(3, (short) 6);
        vc.step();
        vc.step();
        assertEquals(6, vc.state.PC);
    }

    @Test
    public void testInstruction30() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(0, Instruction.compileOpcode("JMP"));
        vc.state.mm.write(1, (short) 6);
        vc.state.mm.write(6, Instruction.compileOpcode("RET"));
        vc.state.mm.write(7, (short) 0);
        vc.step();
        vc.step();
        assertEquals(2, vc.state.PC);
    }

    @Test
    public void testInstruction31() {
        OutputBuffer o = new OutputBuffer();
        VirtualComputer vc = new VirtualComputer(1, o);
        vc.state.mm.write(0, Instruction.compileOpcode("OUTC"));
        vc.state.mm.write(1, (short) 64);
        vc.step();
        assertEquals("@", o.getMessage());
    }

    @Test
    public void testInstruction32() {
        OutputBuffer o = new OutputBuffer();
        VirtualComputer vc = new VirtualComputer(1, o);
        vc.state.mm.write(0, Instruction.compileOpcode("OUTS"));
        vc.state.mm.write(1, (short) 10);
        vc.state.mm.write(10, (short) 0x48);
        vc.state.mm.write(11, (short) 0x49);
        vc.step();
        assertEquals("HI", o.getMessage());
    }

    @Test
    public void testInstruction33() {
        OutputBuffer o = new OutputBuffer();
        VirtualComputer vc = new VirtualComputer(1, o);
        vc.state.registers[0].write( (short) 0x30);
        vc.state.mm.write(0, Instruction.compileOpcode("OUTR"));
        vc.state.mm.write(1, (short) 0);
        vc.step();
        assertEquals("0", o.getMessage());
    }

    @Test
    public void testInstruction34() {
        VirtualComputer vc = new VirtualComputer(1, new OutputBuffer());
        vc.state.mm.write(0, Instruction.compileOpcode("HLT"));
        vc.state.mm.write(1, (short) 0);
        vc.step();
        assertEquals(0, vc.state.PC);
    }
}
