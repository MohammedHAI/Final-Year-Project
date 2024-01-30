package guitest;/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 08/11/2023
 */

import java.util.*;
import java.awt.*;
import javax.swing.*;

// Will be overlayed over the textbox for the code
public class Overlay extends JComponent {
    private JPanel overlayPanel = new JPanel();
    private String explanation = "loops n times";

    public Overlay() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 200));

        overlayPanel.setPreferredSize(new Dimension(200, 200));
        //overlayPanel.setLocation(40, 40);
        overlayPanel.setForeground(Color.magenta);
        this.add(overlayPanel);
        overlayPanel.revalidate();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.MAGENTA);
        //g.fillRect(0, 0, 200, 200);
        g.fillRect(0, 0, overlayPanel.getWidth(), overlayPanel.getHeight());
        //g.fillRect(overlayPanel.getX(), overlayPanel.getY(), overlayPanel.getWidth(), overlayPanel.getHeight());

        g.setColor(Color.BLACK);
        g.drawString(explanation, 40, 40);
    }
}
