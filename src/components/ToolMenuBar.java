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
    public ToolMenuBar() {
        // Create a menu bar and add it to the frame
        JMenuBar menubar = new JMenuBar();

        // Add a "File" menu to the menu bar
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        add(fileMenu);

        ActionListener fileActionListener = (ActionEvent e) -> System.out.println("File menu clicked!");
        FileMenuItem fileMenuItemNewProgram = new FileMenuItem("New program", fileActionListener);
        FileMenuItem fileMenuItemLoad = new FileMenuItem("Load...", fileActionListener);
        FileMenuItem fileMenuItemSave = new FileMenuItem("Save...", fileActionListener);
        FileMenuItem fileMenuItemExit = new FileMenuItem("Exit", (ActionEvent e) -> System.exit(0));

        // Add "File" menu items to file menu
        fileMenu.add(fileMenuItemNewProgram);
        fileMenu.add(fileMenuItemLoad);
        fileMenu.add(fileMenuItemSave);
        fileMenu.addSeparator();
        fileMenu.add(fileMenuItemExit);


        // Add a "Help" menu to the menu bar
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        add(helpMenu);

        // Add "Help" menu items to help menu
        ActionListener helpActionListener = (ActionEvent e) -> System.out.println("Help menu clicked!");
        HelpMenuItem helpMenuItemAbout = new HelpMenuItem("About...", helpActionListener);

        helpMenu.add(helpMenuItemAbout);
    }
}