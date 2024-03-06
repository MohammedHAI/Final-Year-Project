/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created:: 07/12/2023
 */

import java.util.HashMap;

// should really use an enum instead
// not finished defining instructions
class Mnemonics {
    final static byte NOP = 0;   // No OPeration
    final static byte LDR = 1;   // LoaD from Register
    final static byte LDIA = 2;  // LoaD from Immediate value into register A
    final static byte LDIB = 3;  // LoaD from Immediate value into register B
    final static byte LDIC = 4;  // LoaD from Immediate value into register C
    final static byte LDID = 5;  // LoaD from Immediate value into register D
    final static byte LDAB = 6;  // LoaD from Address into register B
    final static byte STR = 7;   // STore into Register
    final static byte STI = 8;   // STore into Immediate value
    final static byte STAA = 9;  // STore from Register A into Address
    final static byte STAC = 10; // STore from Register C into Address
    final static byte ADRC = 11; // ADd two Register values and store in C
    final static byte SUR = 12;  // SUbtract two Register values
    final static byte HLT = -1;  // HaLT
}

// note - to convert byte to unsigned do:
// int unsignedValue = b & 0xFF;

// outlines what an instruction consists of
// instructions are 2 bytes long
public class Instruction {
    final private byte opcode;
    final private byte operand;
    final private static HashMap<String, Byte> mnemonicLookup = new HashMap<>();
    final private static HashMap<Byte, String> byteLookup = new HashMap<>();

    public Instruction(byte opcode, byte operand) {
        this.opcode = opcode;
        this.operand = operand;
    }

    public boolean execute(MainMemory mm, int PC, Register[] registers) {
        switch (opcode) {
            case Mnemonics.NOP:
                break;

            case Mnemonics.LDIA:
                registers[0].write(operand);
            break;

            case Mnemonics.STAA:
                mm.write(operand, registers[0].read());
            break;

            case Mnemonics.STAC:
                mm.write(operand, registers[2].read());
            break;

            case Mnemonics.STR:
                registers[operand >> 4].write(registers[operand & 0x0F].read());
            break;

            case Mnemonics.ADRC:
                registers[2].write((byte) (registers[operand >> 4].read() + registers[operand & 0x07].read()));
            break;

            case Mnemonics.LDAB:
                registers[1].write(mm.read(operand));
            break;

            case Mnemonics.HLT:
                return true;

            default:
                System.out.println("Unknown instruction " + opcode);
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
        mnemonicLookup.put("HLT", Mnemonics.HLT);

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
        byteLookup.put(Mnemonics.HLT, "HLT");
    }

    // Get an instruction as a byte when given its corresponding assembly mnemonic
    public static byte compileOpcode(String mnemonic) {
        return mnemonicLookup.getOrDefault(mnemonic, (byte) 0); // NOP if instruction unknown
    }

    // Get an operand as a byte
    public static byte compileOperand(String data) {
        return Byte.parseByte(data.strip());
    }

    // Get the mnemonic for a byte, if it exists
    public static String decompileOpcode(String opcode) {
        return byteLookup.getOrDefault(Byte.parseByte(opcode), "NOP"); // NOP if instruction unknown
    }

    @Override
    public String toString() {
        return "Opcode: " + opcode + ", Operand: " + operand;
    }
}
