/*
    Author: Mohastate.mmed Abdul Wadud Hamza Al-Islam
    Date created: 06/01/2024
 */
package components;

import java.awt.*;
import javax.swing.*;

// Used by the CodeEditor to display an annotation for a block of code
public class Annotation extends JComponent {
    private String explanation;

    public Annotation(String explanation) {
        this.explanation = explanation;

        // define components
        JLabel label = new JLabel(explanation);
        label.setPreferredSize(new Dimension(200, 200));
        label.setBackground(Color.MAGENTA);

        // add components
        this.add(label);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);


    }
}
