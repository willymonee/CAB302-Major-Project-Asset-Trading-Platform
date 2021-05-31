package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.AssetTrading.BuyOffer;
import ElectronicAssetTradingPlatform.AssetTrading.BuyOfferData;
import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.Map;
import java.util.TreeMap;


public class BuyTabGUI extends JPanel {
    // Global variables
    private OrganisationalUnitMembers loggedInMember;
    private NetworkDataSource data;
    private JPanel wrapper;
    private JPanel orgBuyOffersPanel;
    private JLabel orgBuyOfferLabel;
    private JLabel welcomeMessage;
    private JPanel marketBuyOffersPanel;
    private JScrollPane scrollPanel;
    private JLabel marketBuyOffersLabel;
    private int selectedOfferID;
    private int selectedRow;
    private JTable buyOffersTable;
    private DefaultTableModel model;


    TreeMap<Integer, BuyOffer> buyOffers;


    /**
     * Construct the BuyTab GUI which will display the organisational unit buy offers & market buy offers
     * @param member the member who has logged in
     * @param dataSource the network connection
     */
    public BuyTabGUI(OrganisationalUnitMembers member, NetworkDataSource dataSource) {
        loggedInMember = member;
        data = dataSource;
        // create a wrapper to put elements in
        wrapper = Helper.createPanel(Color.WHITE);
        BoxLayout boxlayout = new BoxLayout(wrapper, BoxLayout.Y_AXIS);
        wrapper.setLayout(boxlayout);
        wrapper.setPreferredSize(new Dimension(850, 600));
        this.add(wrapper);
        // add a welcome message into the wrapper
        welcomeMessage = Helper.createLabel(memberTextDisplay(), 16);
        welcomeMessage.setHorizontalAlignment(JLabel.CENTER);
        welcomeMessage.setBorder(new EmptyBorder(10, 0, 10, 0));
        wrapper.add(welcomeMessage);
        // add org buy offer panel
        orgBuyOffersPanel = Helper.createPanel(Color.WHITE);
        orgBuyOfferLabel = Helper.createLabel(member.getUnitName() + "'s Buy Offers:", 20);
        orgBuyOfferLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        // add a label to the org buy offer panel
        orgBuyOffersPanel.add(orgBuyOfferLabel);
        // add a table to the org buy offer panel
        JTable buyOffersTable = unitBuyOffersTable();
        int buyTableHeight = buyOffersTable.getPreferredSize().height + 25;
        // if the buy table's height is greater than 250
        if (buyTableHeight >= 250) {
            // create scroll panel with table inside
           scrollPanel = new JScrollPane(buyOffersTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
           // set the org buy offer panel to a FIXED 325
           orgBuyOffersPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 325));
           orgBuyOffersPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 325));
           // set the scroll panel to a FIXED 250
           scrollPanel.setPreferredSize(new Dimension(850, 250));
           scrollPanel.setMaximumSize(new Dimension(850, 250));
        }
        else {
            // create scroll panel with table inside, but is not a fixed size
            scrollPanel = new JScrollPane(buyOffersTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            int height = buyOffersTable.getPreferredSize().height + 125;
            // set height of panel to the buy offer table's size + 125 (VARIABLE size)
            orgBuyOffersPanel.setPreferredSize(new Dimension(825, height));
            orgBuyOffersPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
            // set height of scroll panel to a VARIABLE size equal to the buy offers table's height
            scrollPanel.setPreferredSize(new Dimension(850, buyOffersTable.getPreferredSize().height + 25));
        }
        scrollPanel.getViewport().setBackground(Color.WHITE);
        // add the scroll panel to the org buy offer's panel
        orgBuyOffersPanel.add(scrollPanel);
        // add panel to wrapper
        wrapper.add(orgBuyOffersPanel);

        marketBuyOffersPanel = Helper.createPanel(Color.GRAY);
        marketBuyOffersLabel = Helper.createLabel("Market Buy Offers", 20);
        marketBuyOffersPanel.add(marketBuyOffersLabel);
        wrapper.add(marketBuyOffersPanel);
    }

    /**
     * A welcome message once in the BuyTab GUI
     * @return String welcome message including the user's username
     */
    private String memberTextDisplay() {
        String memberTextDisplay = "";
        memberTextDisplay += "Welcome" + loggedInMember.getUsername();
        // this.add(new JTextArea("welcome " + loggedInMember.getUsername()));
        try {
            float credits = data.getCredits(loggedInMember.getUnitName());
            System.out.println(credits);
            memberTextDisplay += "\t" + loggedInMember.getUnitName() + ": " + Float.toString(credits) + " credits";
        }
        catch (DatabaseException e) {
            e.printStackTrace();
        }
        return memberTextDisplay;
    }

    /**
     * Returns a TreeMap of the organisational unit's buy offers
     * @return
     * @throws Exception
     */
    private TreeMap<Integer, BuyOffer> getUnitBuyOffers() throws Exception {
        buyOffers = null;
        buyOffers = BuyOfferData.getInstance().getOrgOffersMap(loggedInMember.getUnitName());
        if (buyOffers == null) {
            throw new Exception(); // maybe don't 'throw' an exception just display nothing
        }
        return buyOffers;

    }

    /**
     * Extract the organisational unit's buy offers from the TreeMap and return it as an array of strings
     * @return Array of org buy offers to be used as input for JTable
     */
    private String[][] getOrgUnitBuyOffersRowData() {
        try {
            buyOffers = getUnitBuyOffers();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        String[][] data = new String[buyOffers.size()][];
        int count = 0;
        for(Map.Entry<Integer, BuyOffer> entry : buyOffers.entrySet()) {
            BuyOffer value = entry.getValue();
            data[count] = new String[] {
                    String.valueOf(value.getOfferID()),
                    value.getAssetName(),
                    String.valueOf(value.getQuantity()),
                    String.valueOf(value.getPricePerUnit()),
                    value.getUsername(),
                    "Edit/Delete"
            };
            count++;
        }
        return data;
    }

    /**
     * Given a table, resize the column widths automatically to fit data inside
     * @param table to be resized
     */
    // https://stackoverflow.com/questions/17627431/auto-resizing-the-jtable-column-widths by Paul Vargas
    public void resizeColumnWidth(JTable table) {
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

    public JTable createTable(String data[][], String columns[], DefaultTableModel tableModel) {
        tableModel = new DefaultTableModel(data, columns);
        JTable table = new JTable(tableModel);
        return table;
    }

    /**
     * Create the org unit's buy offers table
     * @return
     */
    private JTable unitBuyOffersTable() {
        String data[][] = getOrgUnitBuyOffersRowData();
        String columns[] = { "Offer ID", "Asset Name", "Quantity", "Price", "Offer Creator", "Edit/Delete"};
        // model = new DefaultTableModel(data, columns);

        buyOffersTable = createTable(data, columns, model);
        resizeColumnWidth(buyOffersTable);
        buyOffersTable.setRowHeight(25);
        buyOffersTable.setFont(new Font ( "Dialog", Font.PLAIN, 14));

        buyOffersTable.getTableHeader().setPreferredSize(new Dimension(150,25));
        buyOffersTable.getTableHeader().setFont(new Font ( "Dialog", Font.BOLD, 14));
        buyOffersTable.getTableHeader().setReorderingAllowed(false);
        // set button column for Edit/Delete column
        buyOffersTable.getColumn("Edit/Delete").setCellRenderer(new ButtonRenderer());
        buyOffersTable.getColumn("Edit/Delete").setCellEditor(new ButtonEditor(new JCheckBox()));

        // center table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        for(int x = 0; x < columns.length; x++){
            buyOffersTable.getColumnModel().getColumn(x).setCellRenderer( centerRenderer );
        }
        // set an underline for the edit/delete buttons
        // https://forums.codeguru.com/showthread.php?38965-Change-background-color-of-one-column-in-JTable#:~:text=Re%3A%20Change%20background%20color%20of,before%20returning%20the%20renderer%2C%20e.g.
        buyOffersTable.getColumn("Edit/Delete").setCellRenderer(
            new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table,
                                                               Object value,
                                                               boolean isSelected,
                                                               boolean hasFocus,
                                                               int row,
                                                               int column) {
                    setText(value.toString());
                    setHorizontalAlignment(JLabel.CENTER);
                    Font font = buyOffersTable.getFont();
                    setFont(font);
                    Map attributes = font.getAttributes();
                    attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                    setFont(font.deriveFont(attributes));
                    return this;
                }
            });
        // prevent editing of cells in table
        buyOffersTable.setDefaultEditor(Object.class, null);
        // set the preferred size of the table
        Dimension preferredSize = new Dimension(825, buyOffersTable.getRowCount() * 25);

        if (preferredSize.height < 225) {
            buyOffersTable.setPreferredSize(preferredSize);
        }


        buyOffersTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                int column = 0;
                int row = buyOffersTable.getSelectedRow();
                String value = buyOffersTable.getModel().getValueAt(row, column).toString();
                selectedOfferID = Integer.parseInt(value);
                selectedRow  = row;
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        return buyOffersTable;
    }







    // Button Renderer and Editor classes from: https://camposha.info/java-jtable-buttoncolumn-tutorial/

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }


    class ButtonEditor extends DefaultCellEditor {

        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("A");
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                String[] options = {"Edit", "Delete", "Cancel"};
                int result = JOptionPane.showOptionDialog(button, "Edit or Remove Offer","Edit or Remove Offer" , JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,null ,options, options[0]);
                if (result == JOptionPane.YES_OPTION) {
                    System.out.println("edit offer");
                }
                else if (result == JOptionPane.NO_OPTION) {
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove the offer?", "Remove Offer", JOptionPane.YES_NO_OPTION);
                    if(dialogResult == JOptionPane.YES_OPTION){
                        System.out.println("remove offer");
                        BuyOfferData.getInstance().removeOffer(selectedOfferID);
                        model.removeRow(selectedRow);
                    }
                    else {
                        System.out.println("cancel remove offer");
                    }
                }
                else {
                    System.out.println("cancel");
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

}

