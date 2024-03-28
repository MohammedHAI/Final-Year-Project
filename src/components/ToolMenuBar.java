/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 06/02/2024
 */

package components;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Currently for testing
public class ToolMenuBar extends JMenuBar {
    public JMenu fileMenu;
    public JMenu helpMenu;

    public ToolMenuBar() {
        // Add a "File" menu to the menu bar
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        add(fileMenu);

        JMenuItem fileMenuItemNewProgram = new JMenuItem("New program");
        JMenuItem fileMenuItemLoad = new JMenuItem("Load...");
        JMenuItem fileMenuItemSave = new JMenuItem("Save...");
        JMenuItem fileMenuItemExit = new JMenuItem("Exit");

        // Add "File" menu items to file menu
        fileMenu.add(fileMenuItemNewProgram);
        fileMenu.add(fileMenuItemLoad);
        fileMenu.add(fileMenuItemSave);
        fileMenu.addSeparator();
        fileMenu.add(fileMenuItemExit);

        // Add a "Help" menu to the menu bar
        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        // TODO: uncomment for production
        //add(helpMenu);

        JMenuItem helpMenuItemAbout = new JMenuItem("About...");

        // Add "Help" menu items to help menu
        helpMenu.add(helpMenuItemAbout);
    }
}