package guitest;/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 15/11/2023
 */


import javax.swing.*;
import javax.swing.table.TableModel;

// see report for specifications
// contains only a memory table, used for testing the display of memory
// uses a temporary array of bytes
public class MemoryWindow {

    private static void setupMemory(int[][] mainMemory) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                mainMemory[j][i] = i + j * 8;
            }
        }
    }

    public static void main(String[] args) {
        String[] columnNames = {"Base Addr.", "0", "1", "2", "3", "4", "5", "6", "7"};
        int[][] mainMemory = new int[8][8];

        setupMemory(mainMemory);

        Object[][] data = {
                {"0x00", mainMemory[0][0], mainMemory[0][1], mainMemory[0][2], mainMemory[0][3], mainMemory[0][4], mainMemory[0][5], mainMemory[0][6], mainMemory[0][7]},
                {"0x10", mainMemory[1][0], mainMemory[1][1], mainMemory[1][2], mainMemory[1][3], mainMemory[1][4], mainMemory[1][5], mainMemory[1][6], mainMemory[1][7]},
                {"0x18", mainMemory[2][0], mainMemory[2][1], mainMemory[2][2], mainMemory[2][3], mainMemory[2][4], mainMemory[2][5], mainMemory[2][6], mainMemory[2][7]},
                {"0x20", mainMemory[3][0], mainMemory[3][1], mainMemory[3][2], mainMemory[3][3], mainMemory[3][4], mainMemory[3][5], mainMemory[3][6], mainMemory[3][7]},
                {"0x28", mainMemory[4][0], mainMemory[4][1], mainMemory[4][2], mainMemory[4][3], mainMemory[4][4], mainMemory[4][5], mainMemory[4][6], mainMemory[4][7]},
                {"0x30", mainMemory[5][0], mainMemory[5][1], mainMemory[5][2], mainMemory[5][3], mainMemory[5][4], mainMemory[5][5], mainMemory[5][6], mainMemory[5][7]},
                {"0x38", mainMemory[6][0], mainMemory[6][1], mainMemory[6][2], mainMemory[6][3], mainMemory[6][4], mainMemory[6][5], mainMemory[6][6], mainMemory[6][7]},
                {"0x40", mainMemory[7][0], mainMemory[7][1], mainMemory[7][2], mainMemory[7][3], mainMemory[7][4], mainMemory[7][5], mainMemory[7][6], mainMemory[7][7]}
        };

        JFrame frame = new JFrame("Memory Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // defining components

        JTable memoryDisplay = new JTable(data, columnNames);

        JScrollPane scroll = new JScrollPane(memoryDisplay);

        // adding components

        frame.add(scroll);
        frame.pack();
        frame.setVisible(true);
    }
}
