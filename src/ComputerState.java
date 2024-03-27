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
}
