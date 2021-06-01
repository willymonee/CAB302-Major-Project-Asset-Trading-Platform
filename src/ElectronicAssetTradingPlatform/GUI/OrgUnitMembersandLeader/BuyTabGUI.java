package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.AssetTrading.BuyOffer;
import ElectronicAssetTradingPlatform.AssetTrading.BuyOfferData;
import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;
import java.util.TreeMap;


public class BuyTabGUI extends JPanel implements ActionListener {
    // Global variables
    private OrganisationalUnitMembers loggedInMember;
    private NetworkDataSource data;
    private JPanel wrapper;
    private JPanel orgBuyOfferPanel;
    private JLabel orgBuyOfferLabel;
    private JLabel welcomeMessage;
    private JPanel marketBuyOffersPanel;
    private JScrollPane scrollPanelOrgBuyOffers;
    private JScrollPane scrollPanelMarketBuyOffers;
    private JLabel marketBuyOffersLabel;
    private int selectedOrgOfferID;
    private int selectedOrgOfferRow;
    private JTable buyOffersTable;
    private JTable marketBuyOffersTable;
    private DefaultTableModel model;
    private DefaultTableModel marketModel;
    private JButton removeOfferButton;
    private JButton editOfferButton;
    private JPanel orgBuyButtonPanel;


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
        wrapper.setPreferredSize(new Dimension(850, 800));
        this.add(wrapper);
        // add a welcome message into the wrapper
        welcomeMessage = Helper.createLabel(memberTextDisplay(), 16);
        welcomeMessage.setHorizontalAlignment(JLabel.CENTER);
        welcomeMessage.setBorder(new EmptyBorder(10, 0, 10, 0));
        wrapper.add(welcomeMessage);
        // add org buy offer panel
        orgBuyOfferPanel = Helper.createPanel(Color.WHITE);
        orgBuyOfferLabel = Helper.createLabel(member.getUnitName() + "'s Buy Offers:", 20);
        orgBuyOfferLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        // add a label to the org buy offer panel

        orgBuyOfferPanel.add(orgBuyOfferLabel);
        // add a table to the org buy offer panel
        buyOffersTable = unitBuyOffersTable();
        scrollPanelOrgBuyOffers = createScrollPane(buyOffersTable, orgBuyOfferPanel);
        // add the scroll panel to the org buy offer's panel
        orgBuyOfferPanel.add(scrollPanelOrgBuyOffers);
        // add button to org buy offer panel
        orgBuyButtonPanel = Helper.createPanel(Color.WHITE);

        removeOfferButton = createButton("Remove Buy Offer");
        removeOfferButton.setEnabled(false);
        editOfferButton = createButton("Edit Buy Offer");
        orgBuyButtonPanel.add(removeOfferButton, BorderLayout.WEST);
        orgBuyButtonPanel.add(editOfferButton, BorderLayout.EAST);
        orgBuyOfferPanel.add(orgBuyButtonPanel);

        // add org buy offer's panel to wrapper
        wrapper.add(orgBuyOfferPanel);
        // add market buy offer panel
        marketBuyOffersPanel = Helper.createPanel(Color.WHITE);
        marketBuyOffersLabel = Helper.createLabel("Market Buy Offers", 20);
        // add label to market buy offer panel
        marketBuyOffersPanel.add(marketBuyOffersLabel);
        // add a table to the market buy offer's panel
        marketBuyOffersTable = marketBuyOffersTable();
        scrollPanelMarketBuyOffers = createScrollPane(marketBuyOffersTable, marketBuyOffersPanel);
        // add the scroll panel to the org buy offer's panel
        marketBuyOffersPanel.add(scrollPanelMarketBuyOffers);
        // add panel to wrapper
        wrapper.add(marketBuyOffersPanel);
    }


    /**
     * Method that takes a table and places it into a scroll pane
     * Additionally formats the JPanel containing the scroll pane
     * @param table
     * @param panel
     * @return a scroll pane containing the table
     */
    private JScrollPane createScrollPane(JTable table, JPanel panel) {
        int tableHeight = table.getPreferredSize().height + 25;
        JScrollPane scrollPane;
        // if the buy table's height is greater than 250
        if (tableHeight >= 250) {
            // create scroll panel with table inside
            scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            // set the org buy offer panel to a FIXED 325
            panel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 375));
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 375));
            // set the scroll panel to a FIXED 250
            scrollPane.setPreferredSize(new Dimension(850, 250));
            scrollPane.setMaximumSize(new Dimension(850, 250));
        }
        else {
            // create scroll panel with table inside, but is not a fixed size
            scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            int height = table.getPreferredSize().height + 165;
            // set height of panel to the buy offer table's size + 125 (VARIABLE size)
            panel.setPreferredSize(new Dimension(825, height));
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
            // set height of scroll panel to a VARIABLE size equal to the buy offers table's height
            scrollPane.setPreferredSize(new Dimension(850, table.getPreferredSize().height + 25));
        }
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
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
            memberTextDisplay += "\t" + loggedInMember.getUnitName() + ": " + credits + " credits";
        }
        catch (DatabaseException e) {
            e.printStackTrace();
        }
        return memberTextDisplay;
    }

    private JButton createButton(String buttonText) {
        // create a JButton object and store it in a local var
        JButton button = new JButton();
        // Set the button text to that passed in String buttonText
        button.setText(buttonText);
        button.addActionListener(this);
        // Return the JButton
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        String buttonText = "";
        if (src == this.removeOfferButton) {
            JButton btn = (JButton)src;
            int dialogResult = JOptionPane.showConfirmDialog
                    (null, "Are you sure you want to remove the offer?",
                            "Remove Offer", JOptionPane.YES_NO_OPTION);
            if(dialogResult == JOptionPane.YES_OPTION){
                // remove the buy offer
                BuyOfferData.getInstance().removeOffer(selectedOrgOfferID);
                // remove all offers in the table
                int rowCount = model.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    model.removeRow(i);
                }
                // add all the offers back using updated data
                String [][] rowData = getOrgUnitBuyOffersRowData();
                for (int i = 0; i < rowData.length; i++) {
                    model.addRow(rowData[i]);
                }

                rowCount = marketModel.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    marketModel.removeRow(i);
                }

                rowData = getMarketBuyOffersRowData();
                for (int i = 0; i < rowData.length; i++) {
                    marketModel.addRow(rowData[i]);
                }

                //                model.removeRow(selectedOrgOfferRow);
//                for (int i = 0; i < marketModel.getRowCount(); i++) {
//                    System.out.println((String) marketModel.getValueAt(i, 0) + String.valueOf(selectedOrgOfferID));
//                    if (((String) marketModel.getValueAt(i, 0)).equals(String.valueOf(selectedOrgOfferID))) {
//                        marketModel.removeRow(i);
//                        System.out.println("should be removing row" + i);
//                    }
//                }

            }
        }
    }

    private String[][] getMarketBuyOffersRowData() {
        TreeMap<Integer, BuyOffer> marketBuyOffers =  BuyOfferData.getInstance().getMarketBuyOffers();
        String[][] data = new String[marketBuyOffers.size()][];
        int count = 0;
        for(Map.Entry<Integer, BuyOffer> entry : marketBuyOffers.entrySet()) {
            BuyOffer value = entry.getValue();
            data[count] = new String[] {
                    String.valueOf(value.getOfferID()),
                    value.getAssetName(),
                    String.valueOf(value.getQuantity()),
                    String.valueOf(value.getPricePerUnit()),
                    value.getUsername()
            };
            count++;
        }
        return data;
    }

    private JTable marketBuyOffersTable() {
        // create a table
        String data[][] = getMarketBuyOffersRowData();
        String columns[] = { "Offer ID", "Asset Name", "Quantity", "Price", "Offer Creator"};
        marketModel = new DefaultTableModel(data, columns);
        JTable table = new JTable(marketModel);
        formatTable(table);
        return table;
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
                    value.getUsername()
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
    private void resizeColumnWidth(JTable table) {
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
    public void formatTable(JTable table) {
        // format the table
        resizeColumnWidth(table);
        table.setRowHeight(25);
        table.setFont(new Font ( "Dialog", Font.PLAIN, 14));
        table.getTableHeader().setPreferredSize(new Dimension(150,25));
        table.getTableHeader().setFont(new Font ( "Dialog", Font.BOLD, 14));
        table.getTableHeader().setReorderingAllowed(false);
        table.setDefaultEditor(Object.class, null);
        // set the preferred size of the table
        Dimension preferredSize = new Dimension(825, table.getRowCount() * 25);
        if (preferredSize.height < 225) {
            table.setPreferredSize(preferredSize);
        }
        // center table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        for(int x = 0; x < table.getColumnCount(); x++){
            table.getColumnModel().getColumn(x).setCellRenderer( centerRenderer );
        }
    }

    /**
     * Create the org unit's buy offers table
     * @return
     */
    private JTable unitBuyOffersTable() {
        // create a table
        String data[][] = getOrgUnitBuyOffersRowData();
        String columns[] = { "Offer ID", "Asset Name", "Quantity", "Price", "Offer Creator"};
        model = new DefaultTableModel(data, columns);
        JTable table = new JTable(model);
        formatTable(table);
        // add listener to the table
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) { }
            @Override
            public void mousePressed(MouseEvent e) {
                int column = 0;
                int row = table.getSelectedRow();
                if (table.getRowCount() != 0) {
                    try {
                        String value = table.getModel().getValueAt(row, column).toString();
                        selectedOrgOfferID = Integer.parseInt(value);
                        selectedOrgOfferRow = row;
                    } catch (ArrayIndexOutOfBoundsException p) {
                        // do nothing
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                int row = table.getSelectedRow();
                if (row == -1) {
                    removeOfferButton.setEnabled(false);
                }
                else {
                    removeOfferButton.setEnabled(true);
                }

            }
        });
        return table;
    }
}

