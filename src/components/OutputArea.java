/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 13/02/2024
 */

package components;

import javax.swing.*;
import java.awt.*;

// Implements the Output Area component for displaying results and other information
// Consists of:
// - JScrollPane
// - JButton
public class OutputArea extends JComponent {
    public JTextArea textArea;
    public JButton clearButton;
    public JCheckBox debugBox;

    public OutputArea() {
        setLayout(new BorderLayout());

        // define components
        JScrollPane scrollPane = new JScrollPane();
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        JPanel buttonPanel = new JPanel();
        clearButton = new JButton("Clear");
        debugBox = new JCheckBox("Enable Output");

        textArea.setFont(new Font("Serif", Font.PLAIN, 14));
        scrollPane.setPreferredSize(new Dimension(600, 100)); // very important!
        debugBox.setSelected(true);

        // add components
        scrollPane.setViewportView(textArea);
        add(scrollPane, BorderLayout.NORTH);
        buttonPanel.add(clearButton);
        buttonPanel.add(debugBox);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
