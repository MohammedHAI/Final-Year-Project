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

public class VirtualComputer implements Runnable {
    private final int MEMORYSIZE = 256;
    private final int NUMBEROFREGISTERS = 4;

    public MainMemory mm;
    public Register[] registers;
    public int statusFlag;
    public int PC;
    public int SP;
    public double clockSpeed;
    public boolean halted;
    private boolean debug;

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

    // Don't call directly if debug output needed
    @Override
    public void run() {
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
            PC = PC + 2;

            if (debug) {
                System.out.println("halted");
            }
        }
    }

    // Main entry point for debugging
    public void runAndPrint() {
        this.debug = true;
        run();
    }

    public void reset() {
        registers[0].write((byte) 0);
        registers[1].write((byte) 0);
        registers[2].write((byte) 0);
        registers[3].write((byte) 0);

        statusFlag = 0;
        PC = 0;
        SP = 0;
        halted = true;
    }
}
