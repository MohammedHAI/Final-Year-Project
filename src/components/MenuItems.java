/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 06/02/2024
 */

package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

// Contains the menu items for menu bar
// Currently for testing
class FileMenuItem extends JMenuItem {
    public FileMenuItem(String label, ActionListener actionListener) {
        super(label);
        setActionCommand(label);
        addActionListener(actionListener);
    }
}

class HelpMenuItem extends JMenuItem {
    public HelpMenuItem(String label, ActionListener actionListener) {
        super(label);
        setActionCommand(label);
        addActionListener(actionListener);
    }
}