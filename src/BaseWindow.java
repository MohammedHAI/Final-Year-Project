/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 06/01/2024
 */

import components.CodeEditor;

import javax.swing.*;
import java.awt.*;

// Holds the components for the main window. May be renamed to "guitest.MainWindow".
// Has no functionality on its own as it is intended to be the main entry
// point of the application
public class BaseWindow {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Main Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(640, 480));
        frame.setLayout(new BorderLayout());

        // defining components
        CodeEditor editor = new CodeEditor();
        //MemoryViewer viewer = new MemoryViewer();
        //ControlPanel controls = new ControlPanel();
        //RegisterPanel registers = new RegisterPanel();

        // adding components
        frame.add(editor, BorderLayout.WEST);
        //frame.add(viewer, BorderLayout.EAST);
        //frame.add(controls, BorderLayout.NORTH);
        //frame.add(registers, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }
}
