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
    public JFrame frame;
    public CodeEditor editor;
    public MemoryViewer viewer;
    public ControlPanel controls;
    public RegisterPanel registers;
    public OutputArea output;
    public JPanel mainPanel;
    public JPanel viewerPanel;
    public ToolMenuBar menu;

    public BaseWindow(byte[] memory) {
        frame = new JFrame("Main Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1280, 720));
        frame.setLayout(new BorderLayout());
        frame.setResizable(false); // because GridBagLayout fails when resizing

        // defining components
        editor = new CodeEditor();
        viewer = new MemoryViewer(memory);
        controls = new ControlPanel();
        registers = new RegisterPanel();
        output = new OutputArea();

        mainPanel = new JPanel();
        viewerPanel = new JPanel();
        menu = new ToolMenuBar();

        // setting up mainPanel
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        mainPanel.setLayout(gb);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 40, 0);
        mainPanel.add(controls, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(registers, gbc);

        // setting up viewerPanel
        viewerPanel.setLayout(gb);

        gbc.gridx = 0;
        gbc.gridy = 0;
        viewerPanel.add(viewer, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        viewerPanel.add(output, gbc);

        // adding other components

        frame.add(editor, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(viewerPanel, BorderLayout.EAST);
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
