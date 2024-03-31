/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 27/03/2024
 */

// holds all variables needed by the Virtual Computer
// allows passing by reference rather than by value
// essentially a data-only class

public class ComputerState {
    public MainMemory mm;
    public Register[] registers;
    public int statusFlag;
    public int PC;
    public int SP;
    public boolean halted;
    public boolean debug;

    // copies all data, making a new reference
    public ComputerState deepCopy() {
        ComputerState temp = new ComputerState();
        temp.mm = mm.deepCopy();
        Register[] tempRegisters = new Register[registers.length];
        for (int i = 0; i < registers.length; i++) {
            tempRegisters[i] = registers[i].deepCopy();
        }
        temp.registers = tempRegisters;
        temp.statusFlag = statusFlag;
        temp.PC = PC;
        temp.SP = SP;
        temp.halted = true; // to prevent it running unexpectedly
        temp.debug = debug;
        return temp;
    }
}
