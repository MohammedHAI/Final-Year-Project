/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created:: 07/12/2023
 */

// Base class for general and special state.registers

public class Register {
    private final String name;
    private short data;

    public Register(String name) {
        this.name = name;
        data = 0;
    }

    public short read() {
        return data;
    }

    public void write(short newData) {
        data = newData;
    }

    // copies all data, making a new reference
    public Register deepCopy() {
        Register temp = new Register(name);
        temp.write(read());
        return temp;
    }

    @Override
    public String toString() {
        return name + ": " + data;
    }
}
