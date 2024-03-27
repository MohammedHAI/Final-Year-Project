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
    public JButton jumpToButton;
    public JButton resetButton;
    public JSpinner speedSpinner;
    public JPanel panel;

    public ControlPanel() {
        setLayout(new BorderLayout());

        // define components
        runStopButton = new JButton(runState);
        stepForwardsButton = new JButton("Step Forwards");
        jumpToButton = new JButton("Jump to...");
        resetButton = new JButton("Reset");
        JLabel speedLabel = new JLabel("Clock speed (Hz)");
        SpinnerListModel model = new SpinnerListModel();
        speedSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        panel = new JPanel();

        // modify components
        panel.setLayout(new GridLayout(3, 2));

        // add components
        panel.add(runStopButton);
        panel.add(resetButton);
        panel.add(jumpToButton);
        panel.add(stepForwardsButton);
        panel.add(speedLabel);
        panel.add(speedSpinner);
        add(panel);
    }

    // Toggle between "Run" and "Stop"
    public void updateRunLabel(boolean forceRun) {
        if (forceRun) {
            runStopButton.setText(runState);
            return;
        }

        if (runStopButton.getText().equals(runState)) {
            runStopButton.setText(stopState);
        }
        else {
            runStopButton.setText(runState);
        }
    }
}
