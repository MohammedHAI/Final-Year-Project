/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 30/01/2024
 */

package components;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

// Implements the register panel component based on the design in the report
// Consists of (per register):
// - JLabel
// - JTextField
public class RegisterPanel extends JComponent {
    private JLabel[] labels; // holds one label per register
    private JTextField[] textFields; // holds one field per register
    private final int NUMREGISTERS = 8; // TODO: Redesign so that it is suited for special registers

    public JLabel label1 = new JLabel("Register A");
    public JLabel label2 = new JLabel("PC");
    public JLabel label3 = new JLabel("Register B");
    public JLabel label4 = new JLabel("SP");
    public JLabel label5 = new JLabel("Register C");
    public JLabel label6 = new JLabel("Status Flag");
    public JLabel label7 = new JLabel("Register D");
    public JLabel label8 = new JLabel("Halted");

    public JTextField field1 = new JTextField("0");
    public JTextField field2 = new JTextField("0");
    public JTextField field3 = new JTextField("0");
    public JTextField field4 = new JTextField("0");
    public JTextField field5 = new JTextField("0");
    public JTextField field6 = new JTextField("0");
    public JTextField field7 = new JTextField("0");
    public JTextField field8 = new JTextField("Yes");

    // all text data initialised here
    public RegisterPanel() {
        setLayout(new GridLayout(4, 4));

        // defining components

        labels = new JLabel[] {
                label1,
                label2,
                label3,
                label4,
                label5,
                label6,
                label7,
                label8
        };

        textFields = new JTextField[] {
                field1,
                field2,
                field3,
                field4,
                field5,
                field6,
                field7,
                field8
        };

        field8.setEditable(false); // halted register

        // adding components
        for (int i = 0; i < NUMREGISTERS; i++) {
            labels[i].setBorder(new LineBorder(Color.BLACK));
            add(labels[i]);
            add(textFields[i]);
        }
    }
}
