/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created:: 07/12/2023
 */

// simply an array of bytes

public class MainMemory {
    private short[] data;

    public MainMemory(int size) {
        data = new short[size];
    }

    public short read(int index) {
        if (index > -1 && index < data.length) {
            return data[index];
        }
        else { // out of bounds
            return 0;
        }
    }

    public void write(int index, short value) {
        if (index > -1 && index < data.length) {
            data[index] = value;
        }
    }

    public void writeBlock(short[] block) {
        if (block.length > data.length) { // block is larger
            for (int i = 0; i < data.length; i++) {
                data[i] = block[i];
            }
            //System.out.println("Not all data written, block is too large");
        }
        else if (block.length < data.length) { // block is smaller
            for (int i = 0; i < block.length; i++) {
                data[i] = block[i];
            }
        }
        else { // block size matches memory size
            data = block;
        }

    }

    // get data up to a specified index
    public short[] getData(int index) {
        short[] temp = new short[index];

        for (int i = 0; i < temp.length; i++) {
            temp[i] = data[i];
        }

        return temp;
    }

    public int size() {
        return data.length;
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
