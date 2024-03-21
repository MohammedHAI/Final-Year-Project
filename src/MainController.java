/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 14/01/2024
 */

// This class actually runs the virtual computer
// and stitches it together with the GUI

import components.OutputBuffer;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.io.*;

public class MainController {
    public static void main(String[] args) {
        OutputBuffer buffer = new OutputBuffer();
        VirtualComputer vc = new VirtualComputer(1, buffer);
        Thread vcThread = new Thread(vc);
        Instruction.setupLookups();

        // program data
        short[] data = new short[] {0x02, 0x01,
                0x06, 0x0A,
                0x0B, 0x01,
                0x0A, 0x0B,
                (short) 0xFF, 0x00,
                0x01, 0x00
        };

        vc.mm.writeBlock(data);

        // main code, run vc in separate thread from GUI
        BaseWindow bw = new BaseWindow(vc.mm.getData(256));
        startVirtualComputer(vcThread);

        // controls all timing for GUI and virtual computer
        Timer t = new Timer((1 / vc.clockSpeed) * 1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // call any update methods here
                synchronized (vc) {
                    vc.notify();
                    updateTable(bw, vc);
                }
            }
        });

        // test code, make run/stop and reset buttons work
        addControlListeners(bw, vc, t);

        // test code, make memory viewer buttons work, make table tooltip work
        addViewerListeners(bw, vc);

        // test code, makes output clear button work
        addOutputListeners(bw, vc);

        // test code, set up file menu buttons
        addFileMenuListeners(bw);

        // test code, set up help menu buttons
        addHelpMenuListeners(bw);

        // test code, allows table modifications to update memory
        DefaultTableModel model = getTableModel(bw, vc);

        // test code, main loop that handles data transfer between GUI and VC
        while (true) {
            updateRegisters(bw, vc);
            updateTable(bw, vc);
            synchronized (vc) {
                if (vc.debug) {
                    bw.output.textArea.append(buffer.getMessage());
                }
            }
        }
    }

    // for bw.controls
    public static void addControlListeners(BaseWindow bw, VirtualComputer vc, Timer t) {
        bw.controls.runStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // controls thread state in order to simulate halting
                // TODO: update button label

                vc.halted = !vc.halted;
                if (vc.halted) {
                    t.stop();
                }
                else {
                    t.start();
                }
            }
        });

        bw.controls.resetButton.addActionListener(new ActionListener() { // TODO: make VC stop when it has reset
            @Override
            public void actionPerformed(ActionEvent e) {
                t.stop();
                vc.reset();
            }
        });

        // test code, compile code in code editor to memory
        bw.editor.compileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Short> compiledCode = new ArrayList<>();
                ArrayList<String> labels = new ArrayList<>();
                JTextArea textArea = bw.editor.textArea;

                // new code using state machine
                enum STATE {
                    READY,
                    LABEL,
                    OPCODE,
                    OPERAND,
                    STARTCONSTANT,
                    ENDCONSTANT,
                    END
                }
                enum TYPE { // for constants
                    NONE,
                    INT,
                    STRING
                }
                STATE s = STATE.READY;
                TYPE t = TYPE.NONE;
                int constantPos = 0;
                final int CONSTANTBASE = 128; // No restriction on constant area
                int lineNumber = 0;
                String line = "";
                String[] lineComponents = new String[0];
                short opcode = 0;
                short operand = 0;
                short intConstant = 0;
                ArrayList<Short> stringConstant = new ArrayList<>();

                // Implements state machine as seen in report, but handles integers and strings differently, very inefficient
                while (s != STATE.END) {
                    try {
                        line = textArea.getText(textArea.getLineStartOffset(lineNumber), textArea.getLineEndOffset(lineNumber) - textArea.getLineStartOffset(lineNumber));
                        lineComponents = line.split(" +");
                    }
                    catch (BadLocationException badLocationException) { // no more lines to read
                        s = STATE.END;
                    }
                    switch (s) {
                        case READY -> {
                            try {
                                if (lineComponents[0].startsWith(".")) {
                                    labels.add(lineComponents[0]);
                                    lineNumber++;
                                    s = STATE.LABEL;
                                } else if (lineComponents[0].startsWith("$")) { // string constant
                                    t = TYPE.STRING;
                                    s = STATE.STARTCONSTANT;
                                } else if (lineComponents[0].startsWith("#")) { // int constant
                                    int checkInt = Integer.parseInt(line.substring(1)); // check that string wasn't passed
                                    t = TYPE.INT;
                                    s = STATE.STARTCONSTANT;
                                } else if (lineComponents.length < 2) {
                                    lineNumber++; // skip this line
                                }
                                else {
                                    s = STATE.END; // read error, exit immediately
                                }
                            }
                            catch (NumberFormatException numberFormatException) {
                                System.out.println(numberFormatException.getLocalizedMessage());
                                s = STATE.END; // String read as int, etc
                            }
                        }
                        case LABEL -> {
                            opcode = Instruction.compileOpcode(lineComponents[0]); // opcode
                            s = STATE.OPCODE;
                        }
                        case OPCODE -> {
                            if (lineComponents.length < 2) {
                                operand = Instruction.compileOperand("0"); // default value
                            }
                            else {
                                operand = Instruction.compileOperand(lineComponents[1]); // operand
                            }
                            s = STATE.OPERAND;
                        }
                        case OPERAND -> {
                            compiledCode.add(opcode); // needs to be handled using instructionPos
                            compiledCode.add(operand);

                            try {
                                String nextLine = textArea.getText(textArea.getLineStartOffset(lineNumber + 1), textArea.getLineEndOffset(lineNumber + 1) - textArea.getLineStartOffset(lineNumber + 1));

                                if (nextLine.isBlank()) { // blank line, meaning end of code section
                                    lineNumber += 2; // skip blank line
                                    s = STATE.READY;
                                }
                                else { // haven't gone through all lines of current label
                                    lineNumber++;
                                    s = STATE.LABEL;
                                }
                            }
                            catch (BadLocationException badLocationException) {
                                s = STATE.END; // no more lines, end of code
                            }
                        }
                        case STARTCONSTANT -> {
                            lineComponents[0] = lineComponents[0].substring(1) ; // remove $ or #

                            if (t == TYPE.INT) {
                                if (lineComponents[0].isBlank()) {
                                    intConstant = Instruction.compileOperand("0"); // default value
                                }
                                else {
                                    intConstant = Instruction.compileOperand(lineComponents[0]);
                                }
                            }
                            else if (t == TYPE.STRING) {
                                stringConstant.clear(); // start each string from fresh
                                if (lineComponents[0].isBlank()) {
                                    stringConstant.add(Instruction.compileOperand("0"));
                                }
                                else {
                                    // add each byte of string, without whitespace
                                    lineComponents[0] = lineComponents[0].strip();
                                    for (Byte b : lineComponents[0].getBytes(StandardCharsets.UTF_8)) {
                                        stringConstant.add((short) b);
                                    }
                                    stringConstant.add(Instruction.compileOperand("0")); // null terminator
                                }
                            }

                            s = STATE.ENDCONSTANT;
                        }
                        case ENDCONSTANT -> {
                            // deal with integers and strings differently (using t, TYPE)
                            if (t == TYPE.INT) {
                                vc.mm.write(constantPos + CONSTANTBASE, intConstant); // double check that this works
                                constantPos++;
                            }
                            else if (t == TYPE.STRING) {
                                for (Short b : stringConstant) {
                                    vc.mm.write(constantPos + CONSTANTBASE, b);
                                    constantPos++;
                                }
                            }

                            if (lineNumber < textArea.getLineCount() - 1) { // haven't gone through all lines
                                lineNumber++;
                                s = STATE.READY;
                            }
                            else {
                                s = STATE.END; // end of code
                            }
                        }
                    }

                }

                // write instruction code to memory, constants should already be handled
                for (int i = 0; i < compiledCode.size(); i++) {
                    vc.mm.write(i, compiledCode.get(i));
                }
            }
        });

        bw.controls.stepForwardsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // should successfully override the Virtual Computer halted state
                synchronized (vc) {
                    vc.step();
                    vc.halted = true;
                    t.stop();
                }
            }
        });
    }

    // for bw.viewer
    public static void addViewerListeners(BaseWindow bw, VirtualComputer vc) {
        bw.viewer.numberSystemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // test code
                // TODO: implement number system switching
                JDialog d = new JDialog(bw.frame);
                d.setSize(new Dimension(300, 200));
                d.setLocationRelativeTo(null);
                d.add(new JLabel("Temp"));
                d.setVisible(true);
            }
        });

        bw.viewer.decompileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // test code
                JDialog confirmDialog = new JDialog(bw.frame, "Confirm decompilation");
                JTextArea text = new JTextArea("Decompilation will overwrite your current program with the one from memory.\n\nAre you sure you want to do this?");
                JPanel buttonPanel = new JPanel();
                JButton noButton = new JButton("No");
                JButton yesButton = new JButton("Yes");
                confirmDialog.setSize(new Dimension(300, 200));
                confirmDialog.setLocationRelativeTo(null);

                text.setLineWrap(true);
                buttonPanel.setLayout(new FlowLayout());
                noButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        confirmDialog.dispose(); // close immediately
                    }
                });

                yesButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // perform the action before closing
                        JTextArea code = bw.editor.textArea;
                        code.setText(".main"); // clear the current code

                        // this performs the translation from bytes to mnemonics
                        int i = 0;
                        String nextByte;
                        String nextOpcode;
                        String nextOperand;
                        boolean finished = false;
                        while (!finished) {
                            nextByte = (String) bw.viewer.table.getValueAt(i / 16, (i % 16) + 1); // add one to columns because the first one is the address offset
                            nextOpcode = Instruction.decompileOpcode(nextByte); // need as string
                            nextOperand = (String) bw.viewer.table.getValueAt((i + 1) / 16, ((i + 1) % 16) + 1);

                            if (nextOpcode.equals("NOP") || nextOpcode.equals("Unknown")) {
                                finished = true;
                            }
                            else { // not finished
                                code.append("\n");
                            }

                            if (!finished) {
                                code.append(nextOpcode + " ");
                                code.append(nextOperand);
                                i += 2; // jump by opcodes
                            }

                        }

                        confirmDialog.dispose();
                    }
                });

                confirmDialog.add(text, BorderLayout.CENTER);
                buttonPanel.add(noButton);
                buttonPanel.add(yesButton);
                confirmDialog.add(buttonPanel, BorderLayout.SOUTH);
                confirmDialog.setVisible(true);
            }
        });

        // decodes the instruction on mouse hover
        JTable table = bw.viewer.table;
        table.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());

                if (row >= 0 && column >= 1) { // adds tooltip
                    table.setToolTipText(Instruction.decompileOpcode((String) table.getValueAt(row, column)));
                }
                else { // removes tooltip
                    table.setToolTipText(null);
                }
            }
        });
    }

    // for bw.output
    public static void addOutputListeners(BaseWindow bw, VirtualComputer vc) {
        bw.output.clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bw.output.textArea.setText("");
            }
        });

        bw.output.debugButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (vc) {
                    vc.debug = !vc.debug;
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
                bw.editor.textArea.setText(null);
            }
        });
        fileMenuItemLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Load clicked!");

                File selectedFile = chooseFile(bw.frame, false);
                if (selectedFile == null) { return; }

                try {
                    BufferedReader is = new BufferedReader(new FileReader(selectedFile));
                    String allLines = "";
                    while (is.ready()) {
                        allLines += is.readLine() + "\n";
                        bw.editor.textArea.setText(allLines);
                    }

                }
                catch (IOException ioException) {
                    handleIOException(ioException, bw.frame, "Error loading file: " + ioException.getLocalizedMessage());
                }
            }
        });
        fileMenuItemSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Save clicked!");

                File selectedFile = chooseFile(bw.frame, true);
                if (selectedFile == null) { return; }

                try {
                    OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(selectedFile));
                    os.write(bw.editor.textArea.getText());
                    os.close();
                }
                catch (IOException ioException) {
                    handleIOException(ioException, bw.frame, "Error opening file: " + ioException.getLocalizedMessage());
                    System.out.println("Error writing a.txt: " + ioException.getLocalizedMessage());
                }
            }
        });
        fileMenuItemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    // opens a file dialog and returns the one chosen
    public static File chooseFile(Frame frame, boolean save) {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("C:\\Users\\Lenovo\\Desktop\\School\\University\\Year 3 Semester A\\FYP\\Temp Files")); // default path
        if (save) {
            fc.setDialogType(JFileChooser.SAVE_DIALOG);
            fc.setDialogTitle("Save program");
            fc.setApproveButtonText("Save");
        }
        else {
            fc.setDialogTitle("Load program");
            fc.setApproveButtonText("Load");
        }

        if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        }
        else { // bad idea
            return null;
        }
    }

    // popup to be used to handle IOException
    // needs frame and message
    public static void handleIOException(IOException ioException, Frame frame, String errorMessage) {
        JDialog errorDialog = new JDialog(frame, "Error", Dialog.ModalityType.APPLICATION_MODAL); // modality makes the application block
        errorDialog.setSize(400, 200);
        errorDialog.setLocationRelativeTo(null); // center the dialog

        // add an "OK" button
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                errorDialog.dispose(); // Closes the dialog
            }
        });

        // JLabel seems to get cut off
        errorDialog.getContentPane().add(new JLabel(errorMessage), BorderLayout.CENTER);
        errorDialog.getContentPane().add(okButton, BorderLayout.SOUTH);
        errorDialog.setVisible(true);
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

    public static void updateRegisters(BaseWindow bw, VirtualComputer vc) {
        bw.registers.field1.setText(String.valueOf(vc.registers[0].read()));
        bw.registers.field2.setText(String.valueOf(vc.PC));
        bw.registers.field3.setText(String.valueOf(vc.registers[1].read()));
        bw.registers.field4.setText(String.valueOf(vc.SP));
        bw.registers.field5.setText(String.valueOf(vc.registers[2].read()));
        bw.registers.field6.setText(String.valueOf(vc.statusFlag));
        bw.registers.field7.setText(String.valueOf(vc.registers[3].read()));
        bw.registers.field8.setText(vc.halted ? "Yes" : "No"); // show "Yes" if halted, otherwise "No"
    }

    // table reads all bytes from memory
    public static void updateTable(BaseWindow bw, VirtualComputer vc) {
        DefaultTableModel model = (DefaultTableModel) bw.viewer.table.getModel();

        // update table with memory, lazy implementation
        // please don't freak out. all it does is update the table with a string representation of the bytes that have changed in memory
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount() - 1; j++) { // don't include address column
                if (i * 16 + j < vc.mm.size()) { // if index is not out of bounds of memory
                    if (vc.mm.read(i * 16 + j) != Short.parseShort((String) model.getValueAt(i, j + 1))) {// If cell value does not match memory value
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
                    // TODO: is bugged because race condition causes canonical memory to be preferred over any user edits to cells. also empty cell edit causes crash!
                    if (row * 16 + column - 1 < vc.mm.size()) {
                        vc.mm.write(row * 16 + column - 1, Short.parseShort((String) model.getValueAt(row, column)));
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
