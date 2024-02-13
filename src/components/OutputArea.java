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
    public OutputArea() {
        setLayout(new BorderLayout());

        // define components
        JScrollPane scrollPane = new JScrollPane();
        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        JButton clearButton = new JButton("Clear");

        // add components
        scrollPane.setViewportView(textArea);
        add(scrollPane, BorderLayout.NORTH);
        add(clearButton, BorderLayout.SOUTH);
    }
}
