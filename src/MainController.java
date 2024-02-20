/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 14/01/2024
 */

// This class actually runs the virtual computer
// and stitches it together with the GUI

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class MainController {
    public static void main(String[] args) {
        VirtualComputer vc = new VirtualComputer(1);
        Thread vcThread = new Thread(vc);

        // program data
        byte[] data = new byte[] {0x02, 0x01,
                0x06, 0x0A,
                0x0B, 0x01,
                0x0A, 0x0B,
                (byte) 0xFF, 0x00,
                0x01, 0x00
        };

        vc.mm.writeBlock(data);

        // main code, run vc in separate thread from GUI

        BaseWindow bw = new BaseWindow(vc.mm.getData(16));
        startVirtualComputer(vcThread);

        // test code, make run/stop and reset buttons work
        bw.controls.runStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vc.halted = !vc.halted;
                if (!vc.halted) {
                    Thread vcThread = new Thread(vc);
                    startVirtualComputer(vcThread);
                }
            }
        });

        bw.controls.resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vc.reset();
            }
        });

        // test code, compile code in code editor to memory
        bw.editor.compileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Byte> compiledCode = new ArrayList<>();
                JTextArea textArea = bw.editor.textArea;

                // compile instructions to machine code
                int numberOfLines = textArea.getLineCount();
                for (int i = 0; i < numberOfLines; i++) {
                    // Get each line and split it into separate opcode and operand
                    try {
                        String line = textArea.getText(textArea.getLineStartOffset(i), textArea.getLineEndOffset(i) - textArea.getLineStartOffset(i));
                        String[] lineComponents = line.split(" +");

                        // perform instruction lookup Mnemonic -> Byte
                        compiledCode.add(Instruction.compileOpcode(lineComponents[0])); // opcode
                        compiledCode.add(Instruction.compileOperand(lineComponents[1])); // operand
                    }
                    catch (BadLocationException badLocationException) {
                        String line = "";
                    }
                }

                // write code to memory
                for (int i = 0; i < compiledCode.size(); i++) {
                    vc.mm.write(i, compiledCode.get(i));
                }
            }
        });

        // test code, tries to modify GUI
        while (true) {
            // update registers
            bw.registers.field1.setText(String.valueOf(vc.registers[0].read()));
            bw.registers.field2.setText(String.valueOf(vc.PC));
            bw.registers.field3.setText(String.valueOf(vc.registers[1].read()));
            bw.registers.field4.setText(String.valueOf(vc.SP));
            bw.registers.field5.setText(String.valueOf(vc.registers[2].read()));
            bw.registers.field6.setText(String.valueOf(vc.statusFlag));
            bw.registers.field7.setText(String.valueOf(vc.registers[3].read()));
            bw.registers.field8.setText(String.valueOf(vc.halted ? "Yes" : "No")); // show "Yes" if halted, otherwise "No"

            // update memory, lazy
            DefaultTableModel model = bw.viewer.model;
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount() - 1; j++) { // don't include address column
                    if (i * 16 + j < vc.mm.size()) { // if index is not out of bounds of memory
                        model.setValueAt(vc.mm.read(i * 16 + j), i, j + 1); // don't include address column
                    }
                }
            }
            model.fireTableDataChanged();
        }
    }

    public static void startVirtualComputer(Thread vcThread) {
        try {
            vcThread.start(); // will call VirtualComputer.run()
        }
        catch (Exception e) {
            System.out.println("Virtual Computer has crashed!");
            e.printStackTrace();
        }
    }
}
