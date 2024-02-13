/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 30/01/2024
 */

package components;

import javax.swing.*;
import java.awt.*;

// Implements the register panel component based on the design in the report
// Consists of (per register):
// - JLabel
// - JTextField
public class RegisterPanel extends JComponent {
    private JLabel[] labels; // holds one label per register
    private JTextField[] textFields; // holds one field per register
    private final int NUMREGISTERS = 8;

    // all text data initialised here
    public RegisterPanel() {
        setLayout(new GridLayout(4, 4));

        // defining components

        labels = new JLabel[] {
                new JLabel("Register A"),
                new JLabel("Register B"),
                new JLabel("Register C"),
                new JLabel("Register D"),
                new JLabel("Register E"),
                new JLabel("Register F"),
                new JLabel("Register G"),
                new JLabel("Register H")
        };

        textFields = new JTextField[] {
                new JTextField(),
                new JTextField(),
                new JTextField(),
                new JTextField(),
                new JTextField(),
                new JTextField(),
                new JTextField(),
                new JTextField()
        };

        // adding components
        for (int i = 0; i < NUMREGISTERS; i++) {
            add(labels[i]);
            add(textFields[i]);
        }
    }
}
