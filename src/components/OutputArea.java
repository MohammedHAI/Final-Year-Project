/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 13/02/2024
 */

package components;

import com.sun.jdi.FloatType;

import javax.swing.*;
import java.awt.*;

// Implements the Output Area component for displaying results and other information
// Consists of:
// - JScrollPane
// - JButton
public class OutputArea extends JComponent {
    public JTextArea textArea;
    public JButton clearButton;

    public OutputArea() {
        setLayout(new BorderLayout());

        // define components
        JScrollPane scrollPane = new JScrollPane();
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        clearButton = new JButton("Clear");

        scrollPane.setPreferredSize(new Dimension(600, 100)); // very important!

        // add components
        scrollPane.setViewportView(textArea);
        add(scrollPane, BorderLayout.NORTH);
        add(clearButton, BorderLayout.SOUTH);
    }
}
