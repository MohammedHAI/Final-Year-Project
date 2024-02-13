/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 04/02/2024
 */

package components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

// Implements the memory viewer component based on the design in the report
// Consists of:
// -
// -
public class MemoryViewer extends JComponent {
    private JTable table;
    private short[] data; // using short as byte is signed

    public MemoryViewer() {
        setLayout(new BorderLayout());

        // initialise data
        data = new short[256 * 3]; // placeholder data
        for (int i =  0; i < 3; i++) {
            for (int j = 0; j < 256; j++) {
                data[j + (i * 256)] = (short) j;
            }
        }

        // define components
        DefaultTableModel model = new DefaultTableModel(data.length / 16, 17);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel memoryPanel = new JPanel();
        JButton goToButton = new JButton("Go to address...");
        JButton numberSystemButton = new JButton("Change number system");

        // modify components
        // define column headers
        model.setColumnIdentifiers(new String[] {
                "Addr.",
                "0", "1", "2", "3", "4", "5", "6", "7",
                "8", "9", "A", "B", "C", "D", "E", "F"
        });

        // populate table
        for (int i = 0; i < data.length; i++) {
            int row = i / 16;
            int column = i % 16;

            if (i % 16 == 0) {
                model.setValueAt(row * 16, row, 0);
            }

            model.setValueAt(String.valueOf(data[i]), row, column + 1);
        }

        // update column widths
        TableColumn addressColumn = table.getColumnModel().getColumn(0);
        addressColumn.setPreferredWidth(120);

        memoryPanel.setLayout(new FlowLayout());

        // add components
        memoryPanel.add(scrollPane);
        memoryPanel.add(goToButton);
        memoryPanel.add(numberSystemButton);
        add(memoryPanel);
    }
}
