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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Main Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1280, 720));
        frame.setLayout(new BorderLayout());

        // defining components
        CodeEditor editor = new CodeEditor();
        MemoryViewer viewer = new MemoryViewer();
        ControlPanel controls = new ControlPanel();
        RegisterPanel registers = new RegisterPanel();
        OutputArea output = new OutputArea();

        JPanel mainPanel = new JPanel();
        JPanel subPanel = new JPanel();
        ToolMenuBar menu = new ToolMenuBar();

        mainPanel.setLayout(new BorderLayout());
        subPanel.setLayout(new GridLayout(3, 3));

        controls.setPreferredSize(new Dimension(1, 2));
        viewer.setPreferredSize(new Dimension(3, 1));
        output.setPreferredSize(new Dimension(3, 1));

        // adding components
        frame.add(editor, BorderLayout.WEST);
        //frame.add(viewer, BorderLayout.EAST);
        mainPanel.add(controls, BorderLayout.NORTH);
        mainPanel.add(registers, BorderLayout.SOUTH);
        subPanel.add(mainPanel);
        subPanel.add(viewer);
        subPanel.add(output);
        frame.add(subPanel, BorderLayout.CENTER);
        //frame.add(mainPanel, BorderLayout.CENTER);
        //frame.add(output, BorderLayout.SOUTH);
        frame.setJMenuBar(menu);

        frame.pack();
        frame.setVisible(true);
    }
}
