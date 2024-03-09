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

import components.OutputBuffer;

public class VirtualComputer implements Runnable {
    private final int MEMORYSIZE = 256;
    private final int NUMBEROFREGISTERS = 4;

    public MainMemory mm;
    public Register[] registers;
    private OutputBuffer buffer;
    public int statusFlag;
    public int PC;
    public int SP;
    public int clockSpeed;
    public boolean halted;
    public boolean debug;

    public VirtualComputer(int clockSpeed, OutputBuffer buffer) {
        mm = new MainMemory(MEMORYSIZE);
        this.buffer = buffer;

        registers = new Register[NUMBEROFREGISTERS];
        registers[0] = new Register("Register A");
        registers[1] = new Register("Register B");
        registers[2] = new Register("Register C");
        registers[3] = new Register("Register D");

        statusFlag = 0;
        PC = 0;
        SP = 0;
        this.clockSpeed = clockSpeed;
        halted = true;
        debug = true;
    }

    // Don't call directly if debug output needed
    @Override
    public void run() {
        while (true) { // halting is now handled by timer in MainController
            synchronized (this) {
                try {
                    wait(); // requires timer to allow it to continue
                } catch (InterruptedException interruptedException) {
                    System.out.println("CPU interrupted");
                }
            }

            step();

            if (debug && halted) {
                System.out.println("halted");
            }
        }
    }

    // executes one instruction cycle
    public void step() {
        // fetch
        Instruction currentInstruction = new Instruction(mm.read(PC), mm.read(PC + 1));

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
        halted = currentInstruction.execute(mm, PC, registers, buffer);
        if (!halted) {
            synchronized (this) {
                buffer.setMessage(currentInstruction.toString() + "\n");
                buffer.unlockMessage();
            }
            statusFlag &= ~(0b00000001); // clear reset bit (mask NOT reset)
            PC = PC + 2;
        }
        else {
            synchronized (this) {
                buffer.setMessage("Halted.\n");
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

        statusFlag |= 0b00000001; // set reset bit
        PC = 0;
        SP = 0;
        halted = true;
    }
}
