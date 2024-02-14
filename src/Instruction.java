/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created:: 07/12/2023
 */

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

    public Instruction(byte opcode, byte operand) {
        this.opcode = opcode;
        this.operand = operand;
    }

    public boolean execute(MainMemory mm, byte PC, Register[] registers) {
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

    @Override
    public String toString() {
        return "Opcode: " + opcode + ", Operand: " + operand;
    }
}
