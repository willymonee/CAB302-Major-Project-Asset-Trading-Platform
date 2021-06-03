package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.AssetTrading.*;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.TreeMap;

public class SellTabGUI extends JPanel implements ActionListener, MouseListener, ListSelectionListener {
    // Global variables
    private OrganisationalUnitMembers loggedInMember;
    private NetworkDataSource data;
    private JPanel wrapper;
    private JPanel orgSellOfferPanel;
    private JLabel orgSellOfferLabel;
    private JLabel welcomeMessage;
    private JPanel marketSellOffersPanel;
    private JScrollPane scrollPaneOrgSellOffers;
    private JScrollPane scrollPaneMarketSellOffers;
    private JLabel marketSellOffersLabel;
    private int selectedOrgOfferID;
    private int selectedOrgOfferRow;
    private JTable orgSellOffersTable;
    private JTable marketSellOffersTable;
    private DefaultTableModel orgModel;
    private DefaultTableModel marketModel;
    private JButton removeOfferButton;
    private JButton editOfferButton;
    private JPanel orgSellButtonPanel;
    private JButton viewAssetButton;
    private String selectedAsset;

    public SellTabGUI(OrganisationalUnitMembers member, NetworkDataSource dataSource) {
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
        // add org sell offer panel to wrapper
        orgSellOfferPanel = createOrgSellOfferPanel(member);
        wrapper.add(orgSellOfferPanel);
        // add market sell offer panel to wrapper
        marketSellOffersPanel = createMarketSellOfferPanel();
        wrapper.add(marketSellOffersPanel);
    }

    private JPanel createOrgSellOfferPanel(OrganisationalUnitMembers member) {
        // add org sell offer panel
        orgSellOfferPanel = Helper.createPanel(Color.WHITE);
        orgSellOfferLabel = Helper.createLabel(member.getUnitName() + "'s Sell Offers:", 20);
        orgSellOfferLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        orgSellOfferPanel.add(orgSellOfferLabel);
        // add a table to the org buy offer panel
        orgSellOffersTable = unitSellOffersTable();
        scrollPaneOrgSellOffers = Helper.createScrollPane(orgSellOffersTable , orgSellOfferPanel);
        orgSellOfferPanel.add(scrollPaneOrgSellOffers);
        // add remove and edit
        // add remove and edit offer buttons to org buy offer panel
        orgSellButtonPanel = Helper.createPanel(Color.WHITE);
        removeOfferButton = createButton("Remove Buy Offer");
        editOfferButton = createButton("Edit Buy Offer");
        orgSellButtonPanel.add(removeOfferButton, BorderLayout.WEST);
        orgSellButtonPanel.add(editOfferButton, BorderLayout.EAST);
        orgSellOfferPanel.add(orgSellButtonPanel);
        return orgSellOfferPanel;
    }

    private JPanel createMarketSellOfferPanel() {
        // add market buy offer panel
        marketSellOffersPanel = Helper.createPanel(Color.WHITE);
        marketSellOffersLabel = Helper.createLabel("Market Sell Offers", 20);
        // add label to market buy offer panel
        marketSellOffersPanel.add(marketSellOffersLabel);
        // add a table to the market buy offers panel
        marketSellOffersTable = marketSellOffersTable();
        scrollPaneMarketSellOffers = Helper.createScrollPane(marketSellOffersTable, marketSellOffersPanel);
        marketSellOffersPanel.add(scrollPaneMarketSellOffers);
        // add view asset button to market buy offers panel
        viewAssetButton = createButton("View Asset");
        marketSellOffersPanel.add(viewAssetButton);
        return marketSellOffersPanel;
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
                SellOfferData.getInstance().removeOffer(selectedOrgOfferID);
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
        int rowCount = orgModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            orgModel.removeRow(i);
        }
        // add all the offers back using updated data
        String [][] rowData = getOrgSellOffersData();
        for (int i = 0; i < rowData.length; i++) {
            orgModel.addRow(rowData[i]);
        }

        rowCount = marketModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            marketModel.removeRow(i);
        }

        rowData = getMarketSellOffersData();
        for (int i = 0; i < rowData.length; i++) {
            marketModel.addRow(rowData[i]);
        }
    }

    private String[][] getMarketSellOffersData() {
        TreeMap<Integer, SellOffer> marketBuyOffers =  SellOfferData.getInstance().getMarketSellOffers();
        String[][] data = new String[marketBuyOffers.size()][];
        int count = 0;
        for(Map.Entry<Integer, SellOffer> entry : marketBuyOffers.entrySet()) {
            SellOffer value = entry.getValue();
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

    private String[][] getOrgSellOffersData() {
        TreeMap<Integer, SellOffer> marketBuyOffers =  SellOfferData.getInstance().getOrgOffersMap(loggedInMember.getUnitName());
        String[][] data = new String[marketBuyOffers.size()][];
        int count = 0;
        for(Map.Entry<Integer, SellOffer> entry : marketBuyOffers.entrySet()) {
            SellOffer value = entry.getValue();
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
    private JTable unitSellOffersTable() {
        // create a table
        String data[][] = getOrgSellOffersData();
        String columns[] = { "Offer ID", "Asset Name", "Quantity", "Price", "Offer Creator"};
        orgModel = new DefaultTableModel(data, columns);
        JTable table = new JTable(orgModel);
        Helper.formatTable(table);
        // add listener to the table
        table.addMouseListener(this);
        table.getSelectionModel().addListSelectionListener(this);
        return table;
    }

    private JTable marketSellOffersTable() {
        // create a table
        String data[][] = getMarketSellOffersData();
        String columns[] = { "Offer ID", "Asset Name", "Quantity", "Price", "Offer Creator"};
        marketModel = new DefaultTableModel(data, columns);
        JTable table = new JTable(marketModel);
        Helper.formatTable(table);
        table.addMouseListener(this);
        table.getSelectionModel().addListSelectionListener(this);
        return table;
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



    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Object src = e.getSource();
        if (src == orgSellOffersTable) {
            int column = 0;
            int row = orgSellOffersTable.getSelectedRow();
            if (orgSellOffersTable.getRowCount() != 0) {
                try {
                    String value = orgSellOffersTable.getModel().getValueAt(row, column).toString();
                    selectedOrgOfferID = Integer.parseInt(value);
                    selectedOrgOfferRow = row;
                } catch (ArrayIndexOutOfBoundsException p) {
                    // do nothing
                }
            }
        }
        else if (src == marketSellOffersTable) {
            int column = 1;
            int row = marketSellOffersTable.getSelectedRow();
            if (marketSellOffersTable.getRowCount() != 0) {
                try {
                    selectedAsset = marketSellOffersTable.getModel().getValueAt(row, column).toString();
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
        if (src == orgSellOffersTable.getSelectionModel()) {
            int row = orgSellOffersTable.getSelectedRow();
            if (row == -1) {
                removeOfferButton.setEnabled(false);
            }
            else {
                removeOfferButton.setEnabled(true);
            }
        }
        else if (src == marketSellOffersTable.getSelectionModel()) {
            int row = marketSellOffersTable.getSelectedRow();
            if (row == -1) {
                viewAssetButton.setEnabled(false);
            }
            else {
                viewAssetButton.setEnabled(true);
            }
        }
    }
}
