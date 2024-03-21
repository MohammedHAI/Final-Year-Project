/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 04/02/2024
 */

package components;

import guitest.Overlay;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Implements the memory viewer component based on the design in the report
// Consists of:
// -
// -
public class MemoryViewer extends JComponent {
    public JTable table;
    public JButton goToButton;
    public JButton numberSystemButton;
    public JButton decompileButton;
    private int[] data; // using short as byte is signed

    public MemoryViewer(short[] memory) {
        setLayout(new BorderLayout());

        // initialise data
        data = new int[256];

        for (int i = 0; i < memory.length; i++) {
            data[i] = memory[i];
        }

        // define components
        DefaultTableModel model = new DefaultTableModel(data.length / 16, 17);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel memoryPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        numberSystemButton = new JButton("Change number system");
        decompileButton = new JButton("Decompile");

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

        // highlight used portion of memory
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                /*
                    Rows are distinguished by colour according to specifications:
                    Instruction - Yellow
                    Constants - White
                    Stack - Pink
                */

                if (row < (memory.length / 16) / 2) {
                    c.setBackground(Color.YELLOW);
                }
                else if ((row >= (memory.length / 16) / 2) && (row < (memory.length / 16) - 1)) {
                    c.setBackground(Color.WHITE);
                }
                else {
                    c.setBackground(Color.PINK);
                }
                return c;
            }
        });

        // update column widths and row heights
        TableColumn addressColumn = table.getColumnModel().getColumn(0);
        addressColumn.setPreferredWidth(120);
        table.setRowHeight(20);

        memoryPanel.setLayout(new FlowLayout());
        memoryPanel.setPreferredSize(new Dimension(700, 350));

        // setup buttons
        buttonPanel.setLayout(new GridLayout(2, 1));
        buttonPanel.add(numberSystemButton);
        buttonPanel.add(decompileButton);

        // add components
        memoryPanel.add(scrollPane);
        memoryPanel.add(buttonPanel);
        add(memoryPanel);
    }
}
