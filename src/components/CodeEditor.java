/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 06/01/2024
 */
package components;

import java.awt.*;
import javax.swing.*;

// Implements the code editor component based on the design in the report
// Consists of:
// - ScrollPane
// -
public class CodeEditor extends JComponent {
    public CodeEditor() {
        // setup component
        this.setLayout(new BorderLayout());

        // define components
        JScrollPane scrollPane = new JScrollPane();
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        Annotation annotation = new Annotation("test");

        // add components
        scrollPane.setViewportView(textArea);
        this.add(scrollPane, BorderLayout.WEST);
        this.add(annotation, BorderLayout.EAST);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
}