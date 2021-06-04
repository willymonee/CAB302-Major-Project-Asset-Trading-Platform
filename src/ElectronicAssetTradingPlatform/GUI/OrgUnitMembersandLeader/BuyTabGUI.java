package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.AssetTrading.*;
import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * BuyTabGUI class is responsible for displaying the organisational unit's buy offers and all the market buy offers
 * Enables user to remove their org's offers and view assets on the market
 */
public class BuyTabGUI extends JPanel implements ActionListener, MouseListener, ListSelectionListener, ChangeListener {
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
    private JTable orgBuyOffersTable;
    private JTable marketBuyOffersTable;
    private DefaultTableModel model;
    private DefaultTableModel marketModel;
    private JButton removeOfferButton;
    private JButton editOfferButton;
    private JPanel orgBuyButtonPanel;
    private JButton viewAssetButton;
    private String selectedAsset;
    private final int OFFER_ID_COLUMN = 0;
    private final int ASSET_NAME_COLUMN = 1;



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

    /**
     * Create an org buy offer panel which displays the organisational unit's current buy offers
     * @param member logged in
     * @return org buy offer panel
     */
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

    /**
     * Create a market buy offer panel which displays all current market buy offers
     * @return market buy offer panel
     */
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

    /**
     * Create a button given text
     * @param buttonText text to be displayed in the button
     * @return a JButton object
     */
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

    /**
     * Action Listener for JButtons created in createButton for when a particular button is pressed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        // when remove offer button is pressed
        if (src == this.removeOfferButton) {
            String message = "Are you sure you want to remove offer: " + selectedOrgOfferID;
            // open a confirm dialog for removing the selected offer
            int dialogResult = JOptionPane.showConfirmDialog
                    (null, message,
                            "Remove Offer", JOptionPane.YES_NO_OPTION);
            if(dialogResult == JOptionPane.YES_OPTION){
                // remove the buy offer
                BuyOfferData.getInstance().removeOffer(selectedOrgOfferID);
                // update all tables in the buy tab
                updateTables();
            }
        }
        // when the view asset button is pressed
        else if (src == this.viewAssetButton) {
            // open the assetDetailGUI in a new frame which will display the selected asset
            AssetDetailGUI assetDetailGUI = new AssetDetailGUI(loggedInMember, data, new Asset(selectedAsset));
            // add a window listener to the new frame which will update the buy tab's tables when the window is closed
            assetDetailGUI.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosed(WindowEvent e)
                {
                    System.out.println("Closed");
                    updateTables();
                    welcomeMessage.setText(memberTextDisplay());
                    e.getWindow().dispose();
                }
            });
        }
    }

    /**
     * Method which will re-update the data in the tables with data from the database
     */
    private void updateTables() {
        int rowCount = model.getRowCount();
        // remove all rows
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        // add all the offers back using updated data
        String [][] rowData = getOrgUnitBuyOffersRowData();
        for (int i = 0; i < rowData.length; i++) {
            model.addRow(rowData[i]);
        }
        // remove all rows
        rowCount = marketModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            marketModel.removeRow(i);
        }
        // add all the offers back using updated data
        rowData = getMarketBuyOffersRowData();
        for (int i = 0; i < rowData.length; i++) {
            marketModel.addRow(rowData[i]);
        }
    }


    /**
     * Retrieve all the current market buy offers
     * @return a two-dimensional array containing these buy offers
     */
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
     * Extract the organisational unit's buy offers from the TreeMap and return it as an array of strings
     * @return Array of org buy offers to be used as input for JTable
     */
    private String[][] getOrgUnitBuyOffersRowData() {
        TreeMap<Integer, BuyOffer> marketBuyOffers =  BuyOfferData.getInstance().getOrgOffersMap(loggedInMember.getUnitName());
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

    /**
     * Table mouse listeners
     */
    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    // when a table is pressed on
    public void mousePressed(MouseEvent e) {
        Object src = e.getSource();
        // if the table pressed was the org buy offer table
        if (src == orgBuyOffersTable) {
            // select the offerID of that particular selected row
            int column = OFFER_ID_COLUMN;
            int row = orgBuyOffersTable.getSelectedRow();
            if (orgBuyOffersTable.getRowCount() != 0) {
                try {
                    String value = orgBuyOffersTable.getModel().getValueAt(row, column).toString();
                    selectedOrgOfferID = Integer.parseInt(value);
                } catch (ArrayIndexOutOfBoundsException p) {
                    // do nothing
                }
            }
        }
        // if the table pressed was the market sell offer table
        else if (src == marketBuyOffersTable) {
            // select the asset name of that particular row
            int column = ASSET_NAME_COLUMN;
            int row = marketBuyOffersTable.getSelectedRow();
            if (marketBuyOffersTable.getRowCount() != 0) {
                try {
                    selectedAsset = marketBuyOffersTable.getModel().getValueAt(row, column).toString();
                } catch (ArrayIndexOutOfBoundsException p) {
                    // do nothing
                }
            }
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }

    /**
     * Table list selection listener
     */
    @Override
    // when a selection is changed on the table
    public void valueChanged(ListSelectionEvent e) {
        Object src = e.getSource();
        // if selected the org buy offers table
        if (src == orgBuyOffersTable.getSelectionModel()) {
            int row = orgBuyOffersTable.getSelectedRow();
            // if a row is not selected turn off the remove and edit offer buttons
            if (row == -1) {
                removeOfferButton.setEnabled(false);
                editOfferButton.setEnabled(false);
            }
            // enable the buttons once a row is selected
            else {
                removeOfferButton.setEnabled(true);
                editOfferButton.setEnabled(true);
            }
        }
        // if selected value changed in the market buy offers table
        else if (src == marketBuyOffersTable.getSelectionModel()) {
            int row = marketBuyOffersTable.getSelectedRow();
            // if no row is selected then disable the view asset button
            if (row == -1) {
                viewAssetButton.setEnabled(false);
            }
            // if a row is selected then enable it
            else {
                viewAssetButton.setEnabled(true);
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        System.out.println("updating table");
        welcomeMessage.setText(memberTextDisplay());
        updateTables();
    }
}

