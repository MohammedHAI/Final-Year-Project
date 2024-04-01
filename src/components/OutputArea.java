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
    public JButton debugButton;

    public OutputArea() {
        setLayout(new BorderLayout());

        // define components
        JScrollPane scrollPane = new JScrollPane();
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        JPanel buttonPanel = new JPanel();
        clearButton = new JButton("Clear");
        debugButton = new JButton("Toggle Output");

        textArea.setFont(new Font("Serif", Font.PLAIN, 14));
        scrollPane.setPreferredSize(new Dimension(600, 100)); // very important!

        // add components
        scrollPane.setViewportView(textArea);
        add(scrollPane, BorderLayout.NORTH);
        buttonPanel.add(clearButton);
        buttonPanel.add(debugButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
