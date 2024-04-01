/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 07/12/2023
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

    public ComputerState state;
    public int clockSpeed;
    private OutputBuffer buffer;

    public VirtualComputer(int clockSpeed, OutputBuffer buffer) {
        this.buffer = buffer;

        state = new ComputerState();
        state.mm = new MainMemory(MEMORYSIZE);

        state.registers = new Register[NUMBEROFREGISTERS];
        state.registers[0] = new Register("Register A");
        state.registers[1] = new Register("Register B");
        state.registers[2] = new Register("Register C");
        state.registers[3] = new Register("Register D");

        state.statusFlag = 0;
        state.PC = 0;
        state.SP = 0;
        this.clockSpeed = clockSpeed;
        state.halted = true;
        state.debug = true;
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

            /*if (state.debug && state.halted) {
                System.out.println("halted");
            }*/
        }
    }

    // executes one instruction cycle
    public void step() {
        // fetch
        Instruction currentInstruction = new Instruction(state.mm.read(state.PC), state.mm.read(state.PC + 1));

        // old debug output
        /*if (state.debug) {
            System.out.println(currentInstruction);
            System.out.println(state.mm);

            for (int i = 0; i < NUMBEROFREGISTERS; i++) {
                System.out.println(state.registers[i]);
            }

            System.out.println();
        }*/

        // decode and execute
        state.halted = currentInstruction.execute(state, buffer);
        if (!state.halted) {
            state.statusFlag &= ~(0b00000001); // clear reset bit (mask NOT reset)
            state.PC = state.PC + 2;
            if (state.PC >= 256) { state.PC = 0; } // prevent out of bounds
            buffer.setMessage(currentInstruction.toString() + "\n");
            buffer.unlockMessage();
        }
        else {
            synchronized (this) {
                buffer.setMessage("Halted.\n");
            }
        }
    }

    // Main entry point for debugging
    public void runAndPrint() {
        state.debug = true;
        run();
    }

    public void reset() {
        state.registers[0].write((short) 0);
        state.registers[1].write((short) 0);
        state.registers[2].write((short) 0);
        state.registers[3].write((short) 0);

        state.statusFlag |= 0b00000001; // set reset bit
        state.PC = 0;
        state.SP = 0;
        state.halted = true;
    }
}
