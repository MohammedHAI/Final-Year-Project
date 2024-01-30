/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created:: 07/12/2023
 */

// should really use an enum instead
// not finished defining instructions
class Mnemonics {
    final static byte LDR = 0; // LoaD from Register
    final static byte LDIA = 1; // LoaD from Immediate value into register A
    final static byte LDIB = 2; // LoaD from Immediate value into register B
    final static byte LDIC = 3; // LoaD from Immediate value into register C
    final static byte LDID = 4; // LoaD from Immediate value into register D
    final static byte LDAB = 5; // LoaD from Address into register B
    final static byte STR = 6; // STore into Register
    final static byte STI = 7; // STore into Immediate value
    final static byte STA = 8; // STore into Address
    final static byte ADRC = 9; // ADd two Register values and store in C
    final static byte SUR = 10; // SUbtract two Register values
}

// outlines what an instruction consists of
// instructions are 2 bytes long
public class Instruction {
    private byte opcode;
    private byte operand;

    public Instruction(byte opcode, byte operand) {
        this.opcode = opcode;
        this.operand = operand;
    }

    public void execute(MainMemory mm, byte PC, byte[] registers) {
        switch (opcode) {
            case Mnemonics.LDIA:
                registers[0] = operand;
            break;

            case Mnemonics.STA:
                mm.write(operand, registers[0]);
            break;

            case Mnemonics.STR:
                registers[operand >> 4] = registers[operand & 0x0F];
            break;

            case Mnemonics.ADRC:
                registers[2] = (byte) (registers[operand >> 4] + registers[operand & 0x07]);
            break;

            case Mnemonics.LDAB:
                registers[1]  = mm.read(operand);
            break;

            default:
                System.out.println("");
            break;
        }
    }

    @Override
    public String toString() {
        return "Opcode: " + opcode + ", Operand: " + operand;
    }
}
