/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 14/01/2024
 */

// This class actually runs the virtual computer
// and stitches it together with the GUI

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        addControlListeners(bw, vc);

        // test code, set up file menu buttons
        addFileMenuListeners(bw);

        // test code, set up help menu buttons
        addHelpMenuListeners(bw);

        // test code, allows table modifications to update memory
        DefaultTableModel model = getTableModel(bw, vc);

        // test code, main loop that handles data transfer between GUI and VC
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

            updateTable(bw, vc);
        }
    }

    // for bw.controls
    public static void addControlListeners(BaseWindow bw, VirtualComputer vc) {
        bw.controls.runStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vc.halted = !vc.halted;
                if (!vc.halted) {
                    Thread vcThread = new Thread(vc);
                    startVirtualComputer(vcThread);
                }

                // TODO: update button label
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
    }

    // corresponds to the file menu items in ToolMenuBar
    public static void addFileMenuListeners(BaseWindow bw) {
        JMenuItem fileMenuItemNewProgram = (JMenuItem) bw.menu.fileMenu.getPopupMenu().getComponent(0);
        JMenuItem fileMenuItemLoad = (JMenuItem) bw.menu.fileMenu.getPopupMenu().getComponent(1);
        JMenuItem fileMenuItemSave = (JMenuItem) bw.menu.fileMenu.getPopupMenu().getComponent(2);
        JMenuItem fileMenuItemExit = (JMenuItem) bw.menu.fileMenu.getPopupMenu().getComponent(4); // skip 3 as it is a Separator

        fileMenuItemNewProgram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("New program clicked!");
            }
        });
        fileMenuItemLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Load clicked!");
            }
        });
        fileMenuItemSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Save clicked");
            }
        });
        fileMenuItemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    // corresponds to the help menu items in ToolMenuBar
    public static void addHelpMenuListeners(BaseWindow bw) {
        JMenuItem helpMenuItemAbout = (JMenuItem) bw.menu.helpMenu.getPopupMenu().getComponent(0);

        helpMenuItemAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("About clicked!");
            }
        });
    }

    public static void updateTable(BaseWindow bw, VirtualComputer vc) {
        DefaultTableModel model = (DefaultTableModel) bw.viewer.table.getModel();

        // update table with memory, lazy implementation
        // please don't freak out. all it does is update the table with a string representation of the bytes that have changed in memory
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount() - 1; j++) { // don't include address column
                if (i * 16 + j < vc.mm.size()) { // if index is not out of bounds of memory
                    if (vc.mm.read(i * 16 + j) != Byte.parseByte((String) model.getValueAt(i, j + 1))) {// If cell value does not match memory value
                        model.setValueAt(String.valueOf(vc.mm.read(i * 16 + j)), i, j + 1); // write new value to table
                    }
                }
            }
        }
        model.fireTableDataChanged();
    }

    // adds listener for cell updates
    private static DefaultTableModel getTableModel(BaseWindow bw, VirtualComputer vc) {
        DefaultTableModel model = (DefaultTableModel) bw.viewer.table.getModel();

        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                // if a single cell has changed
                if (column != TableModelEvent.ALL_COLUMNS) {
                    System.out.println("Cell at row " + row + ", column " + column + " has changed: " + model.getValueAt(row, column));

                    // adjust to account for address offset
                    // TODO: is bugged because race condition causes canonical memory to be preferred over any user edits to cells
                    if (row * 16 + column - 1 < vc.mm.size()) {
                        vc.mm.write(row * 16 + column - 1, Byte.parseByte((String) model.getValueAt(row, column)));
                    }
                }
            }
        });

        return model;
    }

    // Needed to restart the virtual computer
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
