/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created:: 07/12/2023
 */

// barebones outline

// main memory that can change size
// fixed registers
// 1 status flag
// 1 program counter (up to 2^32)
// 1 stack pointer
// clock speed
// whether halted or not

public class VirtualComputer {
    final private int MEMORYSIZE = 16;
    final private int NUMBEROFREGISTERS = 4;

    public MainMemory mm;
    private byte[] registers;
    private int statusFlag;
    private int PC;
    private int SP;
    private double clockSpeed;
    private boolean halted;

    public VirtualComputer(int clockSpeed) {
        mm = new MainMemory(16);
        registers = new byte[NUMBEROFREGISTERS];
        statusFlag = 0;
        PC = 0;
        SP = 0;
        this.clockSpeed = clockSpeed;
        halted = false;
    }

    public void runAndPrint() {
        while (!halted) {
            try {
                Thread.sleep((long) ((1 / clockSpeed) * 1000)); // multiply by 1000 because Thread.sleep takes milliseconds
            }
            catch (InterruptedException e) {
                System.out.println("CPU interrupted");
                return;
            }

            Instruction currentInstruction = new Instruction(mm.read((byte) PC), mm.read((byte) (PC + 1)));

            System.out.println(currentInstruction);
            System.out.println(mm);

            // temp, halt if opcode 0
            if (mm.read((byte) PC) == 0) {
                System.out.println("halted");
                halted = true;
            }
            if (halted) {
                break;
            }

            currentInstruction.execute(mm, (byte) PC, registers);
            PC = PC + 2;
        }
    }


    // ALL instruction definitions
    public void LOAD(byte operand) {

    }
}
