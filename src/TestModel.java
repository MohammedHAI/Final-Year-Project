/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created:: 07/12/2023
 */

public class TestModel {

    public static void main(String[] args) {
        VirtualComputer vc = new VirtualComputer(10);

        // program does the following:
        //
        // 1. Load value 1 into Register A
        // 2. Load value from address 0x08 into Register B
        // 3. Add Register A and Register B, store result in Register C
        // 4. Store value from Register C to address 0x09
        byte[] data = new byte[] {0x01, 0x01,
            0x05, 0x0A,
            0x09, 0x01,
            0x08, 0x0B,
            0x00, 0x00,
            0x01, 0x00
        };

        for (int i = 0; i < data.length; i++) {
            vc.mm.write(data[i], (byte) i);
        }

        vc.runAndPrint();
    }

}
