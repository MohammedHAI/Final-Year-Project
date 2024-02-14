/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created:: 07/12/2023
 */

// simply an array of bytes

public class MainMemory {
    private byte[] data;

    public MainMemory(int size) {
        data = new byte[size];
    }

    public byte read(byte index) {
        return data[index];
    }

    public void write(byte index, byte value) {
        data[index] = value;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            s.append(String.valueOf(data[i]));
            if (i != data.length - 1) { s.append(", ");}
        }
        return s.toString();
    }
}
