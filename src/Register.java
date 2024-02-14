/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created:: 07/12/2023
 */

// Base class for general and special registers

public class Register {
    private final String name;
    private byte data;

    public Register(String name) {
        this.name = name;
        data = 0;
    }

    public byte read() {
        return data;
    }

    public void write(byte newData) {
        data = newData;
    }

    @Override
    public String toString() {
        return name + ": " + data;
    }
}
