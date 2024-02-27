/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 04/02/2024
 */

package components;

import javax.swing.*;
import java.awt.*;

// Implements the register panel component based on the design in the report
// Consists of:
// - 1 JPanel which holds:
//   - 4 JButtons
public class ControlPanel extends JComponent {
    private final String runState = "Run";
    private final String stopState = "Stop";

    public JButton runStopButton;
    public JButton stepForwardsButton;
    public JButton stepBackwardsButton;
    public JButton resetButton;
    public JPanel panel;

    public ControlPanel() {
        setLayout(new BorderLayout());

        // define components
        runStopButton = new JButton(runState);
        stepForwardsButton = new JButton("Step Forwards");
        stepBackwardsButton = new JButton("Step Backwards");
        resetButton = new JButton("Reset");
        panel = new JPanel();

        // modify components
        panel.setLayout(new GridLayout(2, 2));

        // add components
        panel.add(runStopButton);
        panel.add(resetButton);
        panel.add(stepForwardsButton);
        panel.add(stepBackwardsButton);
        add(panel);
    }
}
