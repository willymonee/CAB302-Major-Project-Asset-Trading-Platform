package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.AssetTrading.Asset;
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
import java.awt.event.*;
import java.util.Map;
import java.util.TreeMap;


public class BuyTabGUI extends JPanel implements ActionListener, MouseListener, ListSelectionListener {
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
    private JTable orgBuyOffersTable;
    private JTable marketBuyOffersTable;
    private DefaultTableModel model;
    private DefaultTableModel marketModel;
    private JButton removeOfferButton;
    private JButton editOfferButton;
    private JPanel orgBuyButtonPanel;
    private JButton viewAssetButton;
    private String selectedAsset;


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

        // add org buy offer's panel to wrapper
        orgBuyOfferPanel = createOrgBuyOfferPanel(member);
        wrapper.add(orgBuyOfferPanel);
        // add market buy offer panel
        marketBuyOffersPanel = createMarketBuyOfferPanel();
        // add market buy offers panel to wrapper
        wrapper.add(marketBuyOffersPanel);
    }

    private JPanel createOrgBuyOfferPanel(OrganisationalUnitMembers member) {
        // add org buy offer panel
        orgBuyOfferPanel = Helper.createPanel(Color.WHITE);
        orgBuyOfferLabel = Helper.createLabel(member.getUnitName() + "'s Buy Offers:", 20);
        orgBuyOfferLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        // add a label to the org buy offer panel

        orgBuyOfferPanel.add(orgBuyOfferLabel);
        // add a table to the org buy offer panel
        orgBuyOffersTable = unitBuyOffersTable();
        scrollPanelOrgBuyOffers = Helper.createScrollPane(orgBuyOffersTable, orgBuyOfferPanel);
        // add the scroll panel to the org buy offer's panel
        orgBuyOfferPanel.add(scrollPanelOrgBuyOffers);
        // add remove and edit offer buttons to org buy offer panel
        orgBuyButtonPanel = Helper.createPanel(Color.WHITE);
        removeOfferButton = createButton("Remove Buy Offer");
        editOfferButton = createButton("Edit Buy Offer");
        orgBuyButtonPanel.add(removeOfferButton, BorderLayout.WEST);
        orgBuyButtonPanel.add(editOfferButton, BorderLayout.EAST);
        orgBuyOfferPanel.add(orgBuyButtonPanel);
        return orgBuyOfferPanel;
    }

    private JPanel createMarketBuyOfferPanel() {
        // add market buy offer panel
        marketBuyOffersPanel = Helper.createPanel(Color.WHITE);
        marketBuyOffersLabel = Helper.createLabel("Market Buy Offers", 20);
        // add label to market buy offer panel
        marketBuyOffersPanel.add(marketBuyOffersLabel);
        // add a table to the market buy offers panel
        marketBuyOffersTable = marketBuyOffersTable();
        scrollPanelMarketBuyOffers = Helper.createScrollPane(marketBuyOffersTable, marketBuyOffersPanel);
        marketBuyOffersPanel.add(scrollPanelMarketBuyOffers);
        // add view asset button to market buy offers panel
        viewAssetButton = createButton("View Asset");
        marketBuyOffersPanel.add(viewAssetButton);
        return marketBuyOffersPanel;

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
        button.setEnabled(false);
        // Return the JButton
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == this.removeOfferButton) {
            String message = "Are you sure you want to remove offer: " + selectedOrgOfferID;
            int dialogResult = JOptionPane.showConfirmDialog
                    (null, message,
                            "Remove Offer", JOptionPane.YES_NO_OPTION);
            if(dialogResult == JOptionPane.YES_OPTION){
                // remove the buy offer
                BuyOfferData.getInstance().removeOffer(selectedOrgOfferID);
                // remove all offers in the table
                updateTables();
            }
        }
        else if (src == this.viewAssetButton) {
            System.out.println("Pressed view asset button");
            AssetDetailGUI assetDetailGUI = new AssetDetailGUI(loggedInMember, data, new Asset(selectedAsset));
            assetDetailGUI.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosed(WindowEvent e)
                {
                    System.out.println("Closed");
                    updateTables();
                    e.getWindow().dispose();
                }
            });
        }
    }


    private void updateTables() {
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
     * Create the org unit's buy offers table
     * @return
     */
    private JTable unitBuyOffersTable() {
        // create a table
        String data[][] = getOrgUnitBuyOffersRowData();
        String columns[] = { "Offer ID", "Asset Name", "Quantity", "Price", "Offer Creator"};
        model = new DefaultTableModel(data, columns);
        JTable table = new JTable(model);
        Helper.formatTable(table);
        // add listener to the table
        table.addMouseListener(this);

        table.getSelectionModel().addListSelectionListener(this);
        return table;
    }

    private JTable marketBuyOffersTable() {
        // create a table
        String data[][] = getMarketBuyOffersRowData();
        String columns[] = { "Offer ID", "Asset Name", "Quantity", "Price", "Offer Creator"};
        marketModel = new DefaultTableModel(data, columns);
        JTable table = new JTable(marketModel);
        Helper.formatTable(table);
        table.addMouseListener(this);
        table.getSelectionModel().addListSelectionListener(this);
        return table;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Object src = e.getSource();
        if (src == orgBuyOffersTable) {
            int column = 0;
            int row = orgBuyOffersTable.getSelectedRow();
            if (orgBuyOffersTable.getRowCount() != 0) {
                try {
                    String value = orgBuyOffersTable.getModel().getValueAt(row, column).toString();
                    selectedOrgOfferID = Integer.parseInt(value);
                    selectedOrgOfferRow = row;
                } catch (ArrayIndexOutOfBoundsException p) {
                    // do nothing
                }
            }
        }
        else if (src == marketBuyOffersTable) {
            int column = 1;
            int row = marketBuyOffersTable.getSelectedRow();
            if (marketBuyOffersTable.getRowCount() != 0) {
                try {
                    selectedAsset = marketBuyOffersTable.getModel().getValueAt(row, column).toString();
                    System.out.println(selectedAsset);
                } catch (ArrayIndexOutOfBoundsException p) {
                    // do nothing
                }
            }
        }
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

    @Override
    public void valueChanged(ListSelectionEvent e) {
        Object src = e.getSource();
        if (src == orgBuyOffersTable.getSelectionModel()) {
            int row = orgBuyOffersTable.getSelectedRow();
            if (row == -1) {
                removeOfferButton.setEnabled(false);
            }
            else {
                removeOfferButton.setEnabled(true);
            }
        }
        else if (src == marketBuyOffersTable.getSelectionModel()) {
            int row = marketBuyOffersTable.getSelectedRow();
            if (row == -1) {
                viewAssetButton.setEnabled(false);
            }
            else {
                viewAssetButton.setEnabled(true);
            }
        }
    }
}

