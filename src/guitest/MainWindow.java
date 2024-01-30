/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 08/11/2023
 */
package guitest;

import java.awt.*;
import javax.swing.*;


// See specifications in the report
// No longer using guitest.Overlay.java but instead adding
// components directly as they wouldn't draw before
public class MainWindow {

    public static void main(String[] args) {
        String exampleCode = "int[] numbers = new int[] {1, 4, 9, 16};\n\nfor (int i = 0; i < n; i++) {\n    System.out.println(numbers[n]);\n}\n\n";
        String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. ";

        JFrame frame = new JFrame("Main Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(640, 480));
        frame.setLayout(new BorderLayout());

        // defining components

        JTextArea codePane = new JTextArea();
        //JTextPane codePane = new JTextPane();
        codePane.setPreferredSize(new Dimension(320, 480));
        codePane.setLineWrap(true);
        codePane.setText(exampleCode + lorem + lorem + lorem + lorem);

        Overlay overlay = new Overlay();
        overlay.setPreferredSize(new Dimension(320, 480));

        JScrollPane scroll = new JScrollPane(codePane);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        Label explanation = new Label("loops n times");
        explanation.setBackground(Color.MAGENTA);

        JPanel overlayPanel = new JPanel();
        overlayPanel.setLayout(new GridLayout(5, 0));
        overlayPanel.add(explanation);

        // adding components

        //frame.add(codePane, BorderLayout.WEST);
        frame.add(scroll, BorderLayout.WEST);
        frame.add(overlayPanel, BorderLayout.EAST);

        //frame.add(overlay, BorderLayout.EAST);
        //frame.setGlassPane(overlay);
        //overlay.setVisible(true);

        frame.pack();
        frame.setVisible(true);
    }
}
