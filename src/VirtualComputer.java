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

// instructions defined in Instruction.java

public class VirtualComputer {
    private final int MEMORYSIZE = 16;
    private final int NUMBEROFREGISTERS = 4;

    public MainMemory mm;
    private Register[] registers;
    private int statusFlag;
    private int PC;
    private int SP;
    private double clockSpeed;
    private boolean halted;

    public VirtualComputer(int clockSpeed) {
        mm = new MainMemory(MEMORYSIZE);

        registers = new Register[NUMBEROFREGISTERS];
        registers[0] = new Register("Register A");
        registers[1] = new Register("Register B");
        registers[2] = new Register("Register C");
        registers[3] = new Register("Register D");

        statusFlag = 0;
        PC = 0;
        SP = 0;
        this.clockSpeed = clockSpeed;
        halted = false;
    }

    public void run(boolean debug) {
        while (!halted) {
            try {
                Thread.sleep((long) ((1 / clockSpeed) * 1000)); // multiply by 1000 because Thread.sleep takes milliseconds
            }
            catch (InterruptedException e) {
                System.out.println("CPU interrupted");
                return;
            }

            // fetch
            Instruction currentInstruction = new Instruction(mm.read((byte) PC), mm.read((byte) (PC + 1)));

            // debug output
            if (debug) {
                System.out.println(currentInstruction);
                System.out.println(mm);

                for (int i = 0; i < NUMBEROFREGISTERS; i++) {
                    System.out.println(registers[i]);
                }

                System.out.println();
            }

            // decode and execute
            halted = currentInstruction.execute(mm, (byte) PC, registers);

            if (!halted) {
                PC = PC + 2;
            }
            else {
                System.out.println("halted");
            }
        }
    }
}
