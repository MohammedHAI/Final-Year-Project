/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 06/01/2024
 */
package components;

import java.awt.*;
import javax.swing.*;

// Implements the code editor component based on the design in the report
// Consists of:
// - JScrollPane
// - JButton
public class CodeEditor extends JComponent {
    public JScrollPane scrollPane;
    public JTextArea textArea;
    public Annotation annotation;
    public JButton compileButton;

    public CodeEditor() {
        // setup component
        this.setLayout(new BorderLayout());

        // define components
        scrollPane = new JScrollPane();
        textArea = new JTextArea();
        annotation = new Annotation("test");
        compileButton = new JButton("Compile");

        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setLineWrap(true);

        // add components
        scrollPane.setViewportView(textArea);
        add(scrollPane, BorderLayout.WEST);
        add(annotation, BorderLayout.EAST);
        add(compileButton, BorderLayout.SOUTH);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
}
