/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created:: 07/12/2023
 */

import components.OutputBuffer;

import java.util.HashMap;

// should really use an enum instead
// not finished defining instructions
class Mnemonics {
    final static short NOP = 0;    // No OPeration
    final static short LDIA = 1;   // LoaD from Immediate value into register A
    final static short LDIB = 2;   // LoaD from Immediate value into register B
    final static short LDIC = 3;   // LoaD from Immediate value into register C
    final static short LDID = 4;   // LoaD from Immediate value into register D
    final static short LDAA = 5;   // LoaD from Address into register A
    final static short LDAB = 6;   // LoaD from Address into register B
    final static short LDAC = 7;   // LoaD from Address into register C
    final static short LDAD = 8;   // LoaD from Address into register D
    final static short STRA = 10;  // STore from register A into the address of Register X
    final static short STRB = 11;  // STore from register B into the address of Register X
    final static short STRC = 12;  // STore from register C into the address of Register X
    final static short STRD = 13;  // STore from register D into the address of Register X
    final static short STAA = 14;  // STore from register A into Address
    final static short STAB = 15;  // STore from register B into Address
    final static short STAC = 16;  // STore from register C into Address
    final static short STAD = 17;  // STore from register D into Address
    final static short MOV = 20;   // MOVe value between registers (actually copies)
    final static short ADD = 21;   // ADD two register values
    final static short SUB = 22;   // SUBtract two register values
    final static short JMP = 30;   // JuMP to address
    final static short CMPE = 41;  // CoMPare Equal
    final static short CMPG = 42;  // CoMPare Greater
    final static short BRC = 43;   // BRanCh
    final static short BEQ = 44;   // Branch EQual
    final static short BNE = 45;   // Branch Not Equal
    final static short BGT = 46;   // Branch GreaTer
    final static short BLT = 47;   // Branch Less Than
    final static short RET = 48;   // RETurn
    final static short OUTC = 50;  // OUTput a Character to the screen
    final static short OUTS = 51;  // OUTput a String to the screen
    final static short OUTR = 52;  // OUTput the value of a Register to the screen
    final static short HLT = 255;  // HaLT program
}

class Status {
    final static short RESET =    0b00000001;
    final static short GREATER =  0b00000010;
    final static short EQUAL =    0b00000100;
    final static short BRANCH =   0b00001000;
    final static short OVERFLOW = 0b00010000;
    final static short ERROR2 =   0b00100000;
    final static short ERROR1 =   0b01000000;
    final static short ZERO =     0b10000000;
}

// outlines what an instruction consists of
// instructions are 2 bytes long
public class Instruction {
    final private short opcode;
    final private short operand;
    final private static HashMap<String, Short> mnemonicLookup = new HashMap<>();
    final private static HashMap<Short, String> byteLookup = new HashMap<>();
    final private static short STACKBASE = 240; // base address of stack

    public Instruction(short opcode, short operand) {
        this.opcode = opcode;
        this.operand = operand;
    }

    public boolean execute(ComputerState state, OutputBuffer buffer) {
        switch (opcode) {
            case Mnemonics.NOP:
                break;

            case Mnemonics.LDIA:
                state.registers[0].write(operand);
                break;

            case Mnemonics.LDIB:
                state.registers[1].write(operand);
                break;

            case Mnemonics.LDIC:
                state.registers[2].write(operand);
                break;

            case Mnemonics.LDID:
                state.registers[3].write(operand);
                break;

            case Mnemonics.LDAA:
                state.registers[0].write(state.mm.read(operand));
                break;

            case Mnemonics.LDAB:
                state.registers[1].write(state.mm.read(operand));
                break;

            case Mnemonics.LDAC:
                state.registers[2].write(state.mm.read(operand));
                break;

            case Mnemonics.LDAD:
                state.registers[3].write(state.mm.read(operand));
                break;

            case Mnemonics.STRA:
                int regX7 = (operand & 0b11);
                state.mm.write(state.registers[regX7].read(), state.registers[0].read());
                break;

            case Mnemonics.STRB:
                int regX8 = (operand & 0b11);
                state.mm.write(state.registers[regX8].read(), state.registers[1].read());
                break;

            case Mnemonics.STRC:
                int regX9 = (operand & 0b11);
                state.mm.write(state.registers[regX9].read(), state.registers[2].read());
                break;

            case Mnemonics.STRD:
                int regX10 = (operand & 0b11);
                state.mm.write(state.registers[regX10].read(), state.registers[3].read());
                break;

            case Mnemonics.STAA:
                state.mm.write(operand, state.registers[0].read());
                break;

            case Mnemonics.STAB:
                state.mm.write(operand, state.registers[1].read());
                break;

            case Mnemonics.STAC:
                state.mm.write(operand, state.registers[2].read());
                break;

            case Mnemonics.STAD:
                state.mm.write(operand, state.registers[3].read());
                break;

            case Mnemonics.MOV:
                int regX = (operand >> 2);
                int regY = (operand & 0b11); // mask first 2 bits
                if (regX < 4) {
                    state.registers[regX].write(state.registers[regY].read()); // copy Y to X
                }
                else {
                    state.statusFlag |= Status.ERROR2;
                }
                break;

            case Mnemonics.ADD:
                int regX2 = (operand >> 4);
                int regY2 = ((operand & 0b1100) >> 2); // mask second set of 2 bits
                int regZ = (operand & 0b11); // mask first 2 bits
                if (regX2 < 4) {
                    short result = (short) (state.registers[regY2].read() + state.registers[regZ].read());
                    if (result < 0 || result > 255) { // check for overflow
                        state.statusFlag |= (Status.OVERFLOW | Status.ERROR1);
                    }
                    else if (result == 0) {
                        state.statusFlag |= Status.ZERO;
                    }
                    state.registers[regX2].write(result); // store result anyway
                }
                else {
                    state.statusFlag |= Status.ERROR2;
                }
                break;

            case Mnemonics.SUB:
                int regX3 = (operand >> 4);
                int regY3 = ((operand & 0b1100) >> 2); // mask second set of 2 bits
                int regZ2 = (operand & 0b11); // mask first 2 bits
                if (regX3 < 4) {
                    short result = (short) (state.registers[regY3].read() - state.registers[regZ2].read());
                    if (result < 0 || result > 255) { // check for overflow
                        state.statusFlag |= (Status.OVERFLOW | Status.ERROR1);
                    }
                    else if (result == 0) {
                        state.statusFlag |= Status.ZERO;
                    }
                    state.registers[regX3].write(result); // store result anyway
                }
                else {
                    state.statusFlag |= Status.ERROR2;
                }
                break;

            case Mnemonics.JMP:
                state.mm.write(STACKBASE + state.SP, (short) state.PC);
                state.SP += 1;
                state.PC = operand;
                if (state.SP > 15) { // stack overflow
                    state.SP = 0;
                    state.statusFlag |= Status.ERROR1;
                }
                else {
                    state.statusFlag |= Status.BRANCH;
                }
                break;

            case Mnemonics.CMPE:
                int regX4 = (operand >> 2);
                int regY4 = (operand & 0b11); // mask first 2 bits
                if (regX4 < 4) {
                    if (state.registers[regX4].read() == state.registers[regY4].read()) {
                        state.statusFlag |= Status.EQUAL;
                    }
                    else {
                        state.statusFlag &= ~Status.EQUAL; // clear equal bit
                    }
                }
                else {
                    state.statusFlag |= Status.ERROR2;
                }
                break;

            case Mnemonics.CMPG:
                int regX5 = (operand >> 2);
                int regY5 = (operand & 0b11); // mask first 2 bits
                if (regX5 < 4) {
                    if (state.registers[regX5].read() > state.registers[regY5].read()) {
                        state.statusFlag |= Status.GREATER;
                    }
                    else {
                        state.statusFlag &= ~Status.GREATER; // clear greater bit
                    }
                }
                else {
                    state.statusFlag |= Status.ERROR2;
                }
                break;

            case Mnemonics.BRC:
                state.PC = operand;
                state.statusFlag |= Status.BRANCH;
                break;

            case Mnemonics.BEQ:
                if ((state.statusFlag & Status.EQUAL) > 0) {
                    state.PC = operand;
                    state.statusFlag |= Status.BRANCH;
                }
                else {
                    state.statusFlag |= Status.ERROR2;
                }
                break;

            case Mnemonics.BNE:
                if ((state.statusFlag & Status.EQUAL) < 1) {
                    state.PC = operand;
                    state.statusFlag |= Status.BRANCH;
                }
                else {
                    state.statusFlag |= Status.ERROR2;
                }
                break;

            case Mnemonics.BGT:
                if ((state.statusFlag & Status.GREATER) > 0) {
                    state.PC = operand;
                    state.statusFlag |= Status.BRANCH;
                }
                else {
                    state.statusFlag |= Status.ERROR2;
                }
                break;

            case Mnemonics.BLT:
                if ((state.statusFlag & Status.GREATER) < 1 && (state.statusFlag & Status.EQUAL) < 1) {
                    state.PC = operand;
                    state.statusFlag |= Status.BRANCH;
                }
                else {
                    state.statusFlag |= Status.ERROR2;
                }
                break;

            case Mnemonics.RET:
                int returnAddress = state.mm.read(STACKBASE + state.SP);
                state.SP -= 1;
                state.PC = returnAddress + 2; // next instruction after return address
                if (state.PC > 255) { // to prevent out of bounds
                    state.PC = 0;
                }
                if (state.SP < 0) { // stack underflow
                    state.SP = 15;
                    state.statusFlag |= Status.ERROR1;
                }
                break;

            case Mnemonics.OUTC:
                buffer.setMessage(new String(Character.toChars(operand))); // get unicode representation of operand
                buffer.lockMessage();
                break;

            case Mnemonics.OUTS:
                StringBuilder sb = new StringBuilder();
                short next = operand; // address of start of string
                short character = state.mm.read(next);
                while (character != 0) { // null terminator
                    if (character == 0x0D || character == 0x0A) { // newline characters must be handled differently
                        sb.append("\n");
                    }
                    else {
                        sb.append(Character.toChars(state.mm.read(next)));
                    }
                    next += 1;
                    character = state.mm.read(next);
                }
                buffer.setMessage(sb.toString());
                buffer.lockMessage();
                break;

            case Mnemonics.OUTR:
                int regX6 = (operand & 0b11);
                buffer.setMessage(new String(Character.toChars(state.registers[regX6].read())));
                buffer.lockMessage();
                break;

            case Mnemonics.HLT:
                return true;

            default:
                //System.out.println("Unknown instruction " + opcode);
            break;
        }
        return false;
    }

    // makes sure the hashmaps are accessible even outside an instance
    public static void setupLookups() {
        mnemonicLookup.put("NOP", Mnemonics.NOP);
        mnemonicLookup.put("LDIA",Mnemonics.LDIA);
        mnemonicLookup.put("LDIB", Mnemonics.LDIB);
        mnemonicLookup.put("LDIC", Mnemonics.LDIC);
        mnemonicLookup.put("LDID", Mnemonics.LDID);
        mnemonicLookup.put("LDAA", Mnemonics.LDAA);
        mnemonicLookup.put("LDAB", Mnemonics.LDAB);
        mnemonicLookup.put("LDAC", Mnemonics.LDAC);
        mnemonicLookup.put("LDAD", Mnemonics.LDAD);
        mnemonicLookup.put("STRA", Mnemonics.STRA);
        mnemonicLookup.put("STRB", Mnemonics.STRB);
        mnemonicLookup.put("STRC", Mnemonics.STRC);
        mnemonicLookup.put("STRD", Mnemonics.STRD);
        mnemonicLookup.put("STAA", Mnemonics.STAA);
        mnemonicLookup.put("STAB", Mnemonics.STAB);
        mnemonicLookup.put("STAC", Mnemonics.STAC);
        mnemonicLookup.put("STAD", Mnemonics.STAD);
        mnemonicLookup.put("MOV", Mnemonics.MOV);
        mnemonicLookup.put("ADD", Mnemonics.ADD);
        mnemonicLookup.put("SUB", Mnemonics.SUB);
        mnemonicLookup.put("JMP", Mnemonics.JMP);
        mnemonicLookup.put("CMPE", Mnemonics.CMPE);
        mnemonicLookup.put("CMPG", Mnemonics.CMPG);
        mnemonicLookup.put("BRC", Mnemonics.BRC);
        mnemonicLookup.put("BEQ", Mnemonics.BEQ);
        mnemonicLookup.put("BNE", Mnemonics.BNE);
        mnemonicLookup.put("BGT", Mnemonics.BGT);
        mnemonicLookup.put("BLT", Mnemonics.BLT);
        mnemonicLookup.put("RET", Mnemonics.RET);
        mnemonicLookup.put("OUTC", Mnemonics.OUTC);
        mnemonicLookup.put("OUTS", Mnemonics.OUTS);
        mnemonicLookup.put("OUTR", Mnemonics.OUTR);
        mnemonicLookup.put("HLT", Mnemonics.HLT);

        // reverse lookup for decompiling, not very efficient
        byteLookup.put (Mnemonics.NOP, "NOP");
        byteLookup.put(Mnemonics.LDIA, "LDIA");
        byteLookup.put(Mnemonics.LDIB, "LDIB");
        byteLookup.put(Mnemonics.LDIC, "LDIC");
        byteLookup.put(Mnemonics.LDID, "LDID");
        byteLookup.put(Mnemonics.LDAA, "LDAA");
        byteLookup.put(Mnemonics.LDAB, "LDAB");
        byteLookup.put(Mnemonics.LDAC, "LDAC");
        byteLookup.put(Mnemonics.LDAD, "LDAD");
        byteLookup.put(Mnemonics.STRA, "STRA");
        byteLookup.put(Mnemonics.STRB, "STRB");
        byteLookup.put(Mnemonics.STRC, "STRC");
        byteLookup.put(Mnemonics.STRD, "STRD");
        byteLookup.put(Mnemonics.STAA, "STAA");
        byteLookup.put(Mnemonics.STAB, "STAB");
        byteLookup.put(Mnemonics.STAC, "STAC");
        byteLookup.put(Mnemonics.STAD, "STAD");
        byteLookup.put(Mnemonics.MOV, "MOV");
        byteLookup.put(Mnemonics.ADD, "ADD");
        byteLookup.put(Mnemonics.SUB, "SUB");
        byteLookup.put(Mnemonics.JMP, "JMP");
        byteLookup.put(Mnemonics.CMPE, "CMPE");
        byteLookup.put(Mnemonics.CMPG, "CMPG");
        byteLookup.put(Mnemonics.BRC, "BRC");
        byteLookup.put(Mnemonics.BEQ, "BEQ");
        byteLookup.put(Mnemonics.BNE, "BNE");
        byteLookup.put(Mnemonics.BGT, "BGT");
        byteLookup.put(Mnemonics.BLT, "BLT");
        byteLookup.put(Mnemonics.RET, "RET");
        byteLookup.put(Mnemonics.OUTC, "OUTC");
        byteLookup.put(Mnemonics.OUTS, "OUTS");
        byteLookup.put(Mnemonics.OUTR, "OUTR");
        byteLookup.put(Mnemonics.HLT, "HLT");
    }

    // Get an instruction as a byte when given its corresponding assembly mnemonic
    public static short compileOpcode(String mnemonic) {
        return mnemonicLookup.getOrDefault(mnemonic, (short) 0); // NOP if instruction unknown
    }

    // Get an operand as a byte
    public static short compileOperand(String data) {
        return Short.parseShort(data.strip());
    }

    // Get the mnemonic for a byte, if it exists
    public static String decompileOpcode(String opcode) {
        return byteLookup.getOrDefault(Short.parseShort(opcode), "Unknown"); // NOP if instruction unknown
    }

    @Override
    public String toString() {
        return "Opcode: " + byteLookup.getOrDefault(opcode, "Unknown") + ", Operand: " + operand;
    }
}
