/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 06/01/2024
 */

import components.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.nio.ByteOrder;

// Holds the components for the main window. May be renamed to "guitest.MainWindow".
// Has no functionality on its own as it is intended to be the main entry
// point of the application
public class BaseWindow {
    public CodeEditor editor;
    public MemoryViewer viewer;
    public ControlPanel controls;
    public RegisterPanel registers;
    public OutputArea output;
    public JPanel mainPanel;
    public ToolMenuBar menu;

    public BaseWindow(byte[] memory) {
        JFrame frame = new JFrame("Main Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1280, 720));
        frame.setLayout(new BorderLayout());

        // defining components
        editor = new CodeEditor();
        viewer = new MemoryViewer(memory);
        controls = new ControlPanel();
        registers = new RegisterPanel();
        output = new OutputArea();

        mainPanel = new JPanel();
        menu = new ToolMenuBar();

        mainPanel.setLayout(new BorderLayout());

        // adding components
        frame.add(editor, BorderLayout.WEST);
        frame.add(viewer, BorderLayout.EAST);
        mainPanel.add(controls, BorderLayout.NORTH);
        mainPanel.add(registers, BorderLayout.SOUTH);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(output, BorderLayout.SOUTH);
        frame.setJMenuBar(menu);

        // temp, test connecting to backend
        JTextField temp = (JTextField) registers.getComponent(1);
        //JTextField temp2 = (JTextField) temp.getComponent(0);
        System.out.println(temp);
        //System.out.println(temp2);

        frame.pack();
        frame.setVisible(true);
    }
}
