/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created:: 07/12/2023
 */

import components.OutputBuffer;

import java.util.HashMap;

// should really use an enum instead
// not finished defining instructions
class Mnemonics {
    final static short NOP = 0;   // No OPeration
    final static short LDR = 1;   // LoaD from Register
    final static short LDIA = 2;  // LoaD from Immediate value into register A
    final static short LDIB = 3;  // LoaD from Immediate value into register B
    final static short LDIC = 4;  // LoaD from Immediate value into register C
    final static short LDID = 5;  // LoaD from Immediate value into register D
    final static short LDAB = 6;  // LoaD from Address into register B
    final static short STR = 7;   // STore into Register (This is stupid, we already have a LoaD operation for that...)
    final static short STI = 8;   // STore into Immediate value
    final static short STAA = 9;  // STore from Register A into Address
    final static short STAC = 10; // STore from Register C into Address
    final static short ADRC = 11; // ADd two Register values and store in C
    final static short SUR = 12;  // SUbtract two Register values
    final static short JMP = 40; // JuMP to address
    final static short OUTC = 50; // OUTput a Character to the screen
    final static short OUTS = 51; // OUTput a String to the screen
    final static short PS = 254; // PauSe and increment PC
    final static short HALT = 255;  // HALT program
}

// outlines what an instruction consists of
// instructions are 2 bytes long
public class Instruction {
    final private short opcode;
    final private short operand;
    final private static HashMap<String, Short> mnemonicLookup = new HashMap<>();
    final private static HashMap<Short, String> byteLookup = new HashMap<>();

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

            case Mnemonics.STAA:
                state.mm.write(operand, state.registers[0].read());
            break;

            case Mnemonics.STAC:
                state.mm.write(operand, state.registers[2].read());
            break;

            case Mnemonics.STR:
                state.registers[operand >> 4].write(state.registers[operand & 0x0F].read());
            break;

            case Mnemonics.ADRC:
                state.registers[2].write((short) (state.registers[operand >> 4].read() + state.registers[operand & 0x07].read()));
            break;

            case Mnemonics.LDAB:
                state.registers[1].write(state.mm.read(operand));
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

            case Mnemonics.JMP:
                state.PC = operand;
            break;

            case Mnemonics.PS:
                state.PC = state.PC + 1;
                return true;

            case Mnemonics.HALT:
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
        mnemonicLookup.put("LDR", Mnemonics.LDR);
        mnemonicLookup.put("LDIA",Mnemonics.LDIA);
        mnemonicLookup.put("LDIB", Mnemonics.LDIB);
        mnemonicLookup.put("LDIC", Mnemonics.LDIC);
        mnemonicLookup.put("LDID", Mnemonics.LDID);
        mnemonicLookup.put("LDAB", Mnemonics.LDAB);
        mnemonicLookup.put("STR", Mnemonics.STR);
        mnemonicLookup.put("STI", Mnemonics.STI);
        mnemonicLookup.put("STAA", Mnemonics.STAA);
        mnemonicLookup.put("STAC", Mnemonics.STAC);
        mnemonicLookup.put("ADRC", Mnemonics.ADRC);
        mnemonicLookup.put("SUR", Mnemonics.SUR);
        mnemonicLookup.put("JMP", Mnemonics.JMP);
        mnemonicLookup.put("PS", Mnemonics.PS);
        mnemonicLookup.put("OUTC", Mnemonics.OUTC);
        mnemonicLookup.put("OUTS", Mnemonics.OUTS);
        mnemonicLookup.put("HLT", Mnemonics.HALT);

        // reverse lookup for decompiling, not very efficient
        byteLookup.put (Mnemonics.NOP, "NOP");
        byteLookup.put (Mnemonics.LDR, "LDR");
        byteLookup.put(Mnemonics.LDIA, "LDIA");
        byteLookup.put(Mnemonics.LDIB, "LDIB");
        byteLookup.put(Mnemonics.LDIC, "LDIC");
        byteLookup.put(Mnemonics.LDID, "LDID");
        byteLookup.put(Mnemonics.LDAB, "LDAB");
        byteLookup.put(Mnemonics.STR, "STR");
        byteLookup.put(Mnemonics.STI, "STI");
        byteLookup.put(Mnemonics.STAA, "STAA");
        byteLookup.put(Mnemonics.STAC, "STAC");
        byteLookup.put(Mnemonics.ADRC, "ADRC");
        byteLookup.put(Mnemonics.SUR, "SUR");
        byteLookup.put(Mnemonics.JMP, "JMP");
        byteLookup.put(Mnemonics.PS, "PS");
        byteLookup.put(Mnemonics.OUTC, "OUTC");
        byteLookup.put(Mnemonics.OUTS, "OUTS");
        byteLookup.put(Mnemonics.HALT, "HLT");
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
