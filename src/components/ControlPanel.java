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
    public JButton compileButton;
    public JPanel panel;

    public ControlPanel() {
        setLayout(new BorderLayout());

        // define components
        runStopButton = new JButton(runState);
        stepForwardsButton = new JButton("Step Forwards");
        stepBackwardsButton = new JButton("Step Backwards");
        compileButton = new JButton("Compile");
        panel = new JPanel();

        // modify components
        panel.setLayout(new GridLayout(2, 2));
        //compileButton.setMargin(new Insets(0, 0, 30, 30));

        // add components
        panel.add(runStopButton);
        panel.add(stepForwardsButton);
        panel.add(stepBackwardsButton);
        panel.add(compileButton);
        add(panel);
    }
}
