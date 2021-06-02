package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class Helper {
    public static JPanel createPanel(Color c) {
        // Create a JPanel object and store it in a local var
        JPanel panel = new JPanel();
        // set the background colour to that passed in c
        panel.setBackground(c);
        // Return the JPanel object
        return panel;
    }

    public static JLabel createLabel(String text, int fontSize) {
        // Create a JLabel object
        JLabel label = new JLabel(text);
        // set the font
        label.setFont(new Font ( "Dialog", Font.BOLD, fontSize));
        // Return the JPanel object
        return label;
    }

    /**
     * Given a table, resize the column widths automatically to fit data inside
     * @param table to be resized
     */
    // https://stackoverflow.com/questions/17627431/auto-resizing-the-jtable-column-widths by Paul Vargas
    private static void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > 300)
                width=300;
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    /**
     * Create and format a table
     */
    public static void formatTable(JTable table) {
        // format the table
        resizeColumnWidth(table);
        table.setRowHeight(25);
        table.setFont(new Font ( "Dialog", Font.PLAIN, 14));
        table.getTableHeader().setPreferredSize(new Dimension(150,25));
        table.getTableHeader().setFont(new Font ( "Dialog", Font.BOLD, 14));
        table.getTableHeader().setReorderingAllowed(false);
        table.setDefaultEditor(Object.class, null);
        // center table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        for(int x = 0; x < table.getColumnCount(); x++){
            table.getColumnModel().getColumn(x).setCellRenderer( centerRenderer );
        }
    }


    /**
     * Method that takes a table and places it into a scroll pane
     * Additionally formats the JPanel containing the scroll pane
     * @param table
     * @param panel
     * @return a scroll pane containing the table
     */
    public static JScrollPane createScrollPane(JTable table, JPanel panel) {
        JScrollPane scrollPane;
        // create scroll panel with table inside
        scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // set the org buy offer panel to a FIXED 325
        panel.setPreferredSize(new Dimension(825, 365));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 365));
        // set the scroll panel to a FIXED 250
        scrollPane.setPreferredSize(new Dimension(850, 250));
        scrollPane.setMaximumSize(new Dimension(850, 250));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }
}
