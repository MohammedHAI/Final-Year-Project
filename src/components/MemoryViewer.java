/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 04/02/2024
 */

package components;

import guitest.Overlay;
import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HexFormat;

// Implements the memory viewer component based on the design in the report
// Consists of:
// -
// -
public class MemoryViewer extends JComponent {
    public JTable table;
    public JButton goToButton;
    public JButton numberSystemButton;
    public JButton decompileButton;
    public boolean useHexadecimal = false;
    private int[] data; // using short as byte is signed
    private int PC = 0; // so the user can see where execution is
    private final String[] decimalIdentifiers = new String[] {
            "Addr.",
            "0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "10", "11", "12", "13", "14", "15"
    };
    private final String[] hexadecimalIdentifiers = new String[] {
            "Addr.",
            "0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "A", "B", "C", "D", "E", "F"
    };

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
        if (useHexadecimal) {
            model.setColumnIdentifiers(hexadecimalIdentifiers);
        }
        else {
            model.setColumnIdentifiers(decimalIdentifiers);
        }

        // populate table
        for (int i = 0; i < data.length; i++) {
            int row = i / 16;
            int column = i % 16;

            if (i % 16 == 0 && useHexadecimal) {
                model.setValueAt(Integer.toHexString(row * 16), row, 0);
            }
            else if (i % 16 == 0 && !useHexadecimal)
            {
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


                if (c instanceof JLabel) {
                    JLabel label = (JLabel) c;
                    // highlight PC cell
                    if (row == (PC / 16) && column == (PC % 16) + 1) { // + 1 because of the base address column
                        label.setBorder(new LineBorder(Color.BLACK));
                    }
                    else {
                        label.setBorder(null);
                    }
                }


                return c;
            }
        });

        // update column widths and row heights
        TableColumn addressColumn = table.getColumnModel().getColumn(0);
        addressColumn.setPreferredWidth(120);
        table.setRowHeight(20);

        // prevent column resizing
        for (int column = 0; column < table.getColumnModel().getColumnCount(); column++) {
            table.getColumnModel().getColumn(column).setResizable(false);
        }

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

    // updates PC by passing VC's value to it
    public void updatePC(int newPC) {
        PC = newPC;
    }

    // Changes table headings and values to either decimal or hexadecimal
    /*public void updateTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        if (useHexadecimal) {
            model.setColumnIdentifiers(hexadecimalIdentifiers);
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    model.setValueAt();
                }
            }
        }
        else {
            model.setColumnIdentifiers(decimalIdentifiers);
        }

        table.setModel(model);
    }*/
}
