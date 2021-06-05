package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.AssetTrading.*;
import ElectronicAssetTradingPlatform.Exceptions.EmptyFieldException;
import ElectronicAssetTradingPlatform.GUI.GUI;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;


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
 * SellTabGUI class is responsible for displaying the organisational unit's sell offers and all the market sell offers
 * Enables user to remove their org's offers and view assets on the market
 */
public class SellTabGUI extends JPanel implements ActionListener, MouseListener, ListSelectionListener, ChangeListener {
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
    private JTable orgSellOffersTable;
    private JTable marketSellOffersTable;
    private DefaultTableModel orgModel;
    private DefaultTableModel marketModel;
    private JButton removeOfferButton;
    private JButton editOfferButton;
    private JPanel orgSellButtonPanel;
    private JButton viewAssetButton;
    private String selectedAsset;
    private String selectedAssetTableOne;
    private int editTabCurrentQuantity;
    private double editTabCurrentPrice;
    private final int OFFER_ID_COLUMN = 0;
    private final int ASSET_NAME_COLUMN = 1;

    /**
     * Construct the SellTab GUI which will display the organisational unit buy offers & market buy offers
     * @param member the member who has logged in
     * @param dataSource the network connection
     */
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

    /**
     * Create an org sell offer panel which displays the organisational unit's current sell offers
     * @param member logged in
     * @return org sell offer panel
     */
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
        removeOfferButton = createButton("Remove Sell Offer");
        editOfferButton = createButton("Edit Sell Offer");
        orgSellButtonPanel.add(removeOfferButton, BorderLayout.WEST);
        orgSellButtonPanel.add(editOfferButton, BorderLayout.EAST);
        orgSellOfferPanel.add(orgSellButtonPanel);
        return orgSellOfferPanel;
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
     * Create a market sell offer panel which displays all current market sell offers
     * @return market sell offer panel
     */
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
        // add an action listener
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
        // when the remove offer button is pressed
        if (src == this.removeOfferButton) {
            String message = "Are you sure you want to remove offer: " + selectedOrgOfferID;
            // open a confirm dialog for removing the selected offer
            int dialogResult = JOptionPane.showConfirmDialog(null, message,
                            "Remove Offer", JOptionPane.YES_NO_OPTION);
            if(dialogResult == JOptionPane.YES_OPTION){
                // remove the sell offer
                SellOfferData.getInstance().removeOffer(selectedOrgOfferID);
                // update all tables in the sell tab
                updateTables();
            }
        }
        // when the view asset button is pressed
        else if (src == this.viewAssetButton) {
            // open the assetDetailGUI in a new frame which will display the selected asset
            AssetDetailGUI assetDetailGUI = new AssetDetailGUI(loggedInMember, data, new Asset(selectedAsset));
            // add a window listener to the new frame which will update the sell tab's tables when the window is closed
            assetDetailGUI.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosed(WindowEvent e)
                {
                    System.out.println("Closed");
                    updateTables();
                    updateMemberTextDisplay();
                    e.getWindow().dispose();
                }
            });
        }

        else if (src == this.editOfferButton) {
            EditSellOfferGUI editGui = new EditSellOfferGUI(data, loggedInMember, new Asset(selectedAssetTableOne),
                                                            editTabCurrentQuantity, editTabCurrentPrice, selectedOrgOfferID);
            updateTables();
        }
    }


    /**
     * Method which will re-update the data in the tables with data from the database
     */
    private void updateTables() {
        int rowCount = orgModel.getRowCount();
        // remove all rows
        for (int i = rowCount - 1; i >= 0; i--) {
            orgModel.removeRow(i);
        }
        // add all the offers back using updated data
        String [][] rowData = getOrgSellOffersData();
        for (int i = 0; i < rowData.length; i++) {
            orgModel.addRow(rowData[i]);
        }
        // remove all rows
        rowCount = marketModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            marketModel.removeRow(i);
        }
        // add all the offers back using updated data
        rowData = getMarketSellOffersData();
        for (int i = 0; i < rowData.length; i++) {
            marketModel.addRow(rowData[i]);
        }
    }

    /**
     * Retrieve all the current market sell offers
     * @return a two-dimensional array containing these sell offers
     */
    private String[][] getMarketSellOffersData() {
        TreeMap<Integer, SellOffer> marketSellOffers =  SellOfferData.getInstance().getMarketSellOffers();
        String[][] data = new String[marketSellOffers.size()][];
        int count = 0;
        for(Map.Entry<Integer, SellOffer> entry : marketSellOffers.entrySet()) {
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
     * Retrieve all the org sell offers
     * @return a two-dimensional array containing these sell offers
     */
    private String[][] getOrgSellOffersData() {
        TreeMap<Integer, SellOffer> marketSellOffers =  SellOfferData.getInstance().getOrgOffersMap(loggedInMember.getUnitName());
        String[][] data = new String[marketSellOffers.size()][];
        int count = 0;
        for(Map.Entry<Integer, SellOffer> entry : marketSellOffers.entrySet()) {
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
     * Create the org unit's sell offers table
     * @return Org unit sell offer table
     */
    private JTable unitSellOffersTable() {
        // create a table
        String data[][] = getOrgSellOffersData();
        String columns[] = { "Offer ID", "Asset Name", "Quantity", "Price", "Offer Creator"};
        orgModel = new DefaultTableModel(data, columns);
        JTable table = new JTable(orgModel);
        // format table
        Helper.formatTable(table);
        // add listeners to the table
        table.addMouseListener(this);
        table.getSelectionModel().addListSelectionListener(this);
        return table;
    }

    /**
     * Create the market sell offers table
     * @return market sell offer table
     */
    private JTable marketSellOffersTable() {
        // create a table
        String data[][] = getMarketSellOffersData();
        String columns[] = { "Offer ID", "Asset Name", "Quantity", "Price", "Offer Creator"};
        marketModel = new DefaultTableModel(data, columns);
        JTable table = new JTable(marketModel);
        // format table
        Helper.formatTable(table);
        // add listeners to table
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
        // if the table pressed was the org sell offer table
        if (src == orgSellOffersTable) {
            // select the offerID of that particular selected row
            int column = OFFER_ID_COLUMN;
            int row = orgSellOffersTable.getSelectedRow();
            if (orgSellOffersTable.getRowCount() != 0) {
                try {
                    String value = orgSellOffersTable.getModel().getValueAt(row, column).toString();
                    selectedOrgOfferID = Integer.parseInt(value);
                    selectedAssetTableOne = SellOfferData.getInstance().getOffer(selectedOrgOfferID).getAssetName();
                    editTabCurrentQuantity = SellOfferData.getInstance().getOffer(selectedOrgOfferID).getQuantity();
                    editTabCurrentPrice = SellOfferData.getInstance().getOffer(selectedOrgOfferID).getPricePerUnit();
                } catch (ArrayIndexOutOfBoundsException p) {
                    // do nothing
                }
            }
        }
        // if the table pressed was the market sell offer table
        else if (src == marketSellOffersTable) {
            // select the asset name of that particular row
            int column = ASSET_NAME_COLUMN;
            int row = marketSellOffersTable.getSelectedRow();
            if (marketSellOffersTable.getRowCount() != 0) {
                try {
                    selectedAsset = marketSellOffersTable.getModel().getValueAt(row, column).toString();
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
    // when a selection is changed on the table e.g. selecting another row
    public void valueChanged(ListSelectionEvent e) {
        Object src = e.getSource();
        // if selected the org sell offers table
        if (src == orgSellOffersTable.getSelectionModel()) {
            int row = orgSellOffersTable.getSelectedRow();
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
        // if selected value changed in the market sell offers table
        else if (src == marketSellOffersTable.getSelectionModel()) {
            int row = marketSellOffersTable.getSelectedRow();
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


    private void updateMemberTextDisplay() {
        welcomeMessage.setText(memberTextDisplay());
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        System.out.println("updating table");
        updateMemberTextDisplay();
        updateTables();
    }

    private class EditSellOfferGUI extends JFrame {
        private OrganisationalUnitMembers loggedInMember;
        private NetworkDataSource net;
        private Asset asset;
        private int quantity;
        private double price;
        private int listingID;

        private JTextField listQuantity;
        private JTextField listPrice;
        private JButton relistBtn;
        private JTextArea messaging;


        /**
         * Constructor for Editing a SellOffer
         * @param data          The network connection
         * @param member        The logged in org unit member
         * @param asset         The specified asset to edit the listing for
         * @param currentQuant  The unedited quantity the sell offer is listed at
         * @param currentPrice  The unedited price the sell offer is listed at
         * @param listingID     The id for the listing
         */

        // Maybe param is also previous quantity/ price
        public EditSellOfferGUI(NetworkDataSource data, OrganisationalUnitMembers member, Asset asset,
                               int currentQuant, double currentPrice, int listingID) {
            net = data;
            loggedInMember = member;
            this.asset = asset;
            quantity = currentQuant;
            price = currentPrice;
            this.listingID = listingID;


            initUI();

            // add window close listener
            addWindowListener(new ClosingListener());

            setTitle("Edit Sell Listing");
            setMinimumSize(new Dimension(400, 300));
            pack();
            setVisible(true);
        }

        private void initUI() {
            Container contentPane = this.getContentPane();
            contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

            contentPane.add(Box.createVerticalStrut(20));
            contentPane.add(panelText());
            contentPane.add(editAssetPanel());


        }

        public JPanel panelText() {
            JPanel panel = new JPanel();
            FlowLayout layout = new FlowLayout(5);
            panel.setLayout(layout);
            JLabel titleLabel = Helper.createLabel("Edit Sell Order - " + asset.getAssetName(), 18);
            panel.add(titleLabel);
            return panel;


        }

        public JPanel editAssetPanel() {
            JPanel displayPanel = new JPanel();
            GroupLayout layout = new GroupLayout(displayPanel);
            displayPanel.setLayout(layout);

            // Turn on automatically adding gaps between components
            layout.setAutoCreateGaps(true);

            // Turn on automatically creating gaps between components that touch
            // the edge of the container and the container.
            layout.setAutoCreateContainerGaps(true);


            JLabel quantityLabel = Helper.createLabel("Quantity:", 12);
            JLabel priceLabel = Helper.createLabel("Price:", 12);



            listQuantity = new JTextField(Integer.toString(quantity), 10);
            listPrice = new JTextField(Double.toString(price),10);
            messaging = new JTextArea();
            messaging.setEditable(false);
            messaging.setLineWrap(true);
            messaging.setWrapStyleWord(true);

            relistBtn = new JButton("RELIST");
            relistBtn.addActionListener(new ButtonListener());

            // Group for Horizontal Axis
            GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
            hGroup.addGroup(layout.createParallelGroup()
                    .addComponent(quantityLabel)
                    .addComponent(priceLabel));

            hGroup.addGroup(layout.createParallelGroup()
                    .addComponent(listQuantity)
                    .addComponent(listPrice)
                    .addComponent(relistBtn, GroupLayout.Alignment.CENTER)
                    .addComponent(messaging, GroupLayout.Alignment.CENTER));
            layout.setHorizontalGroup(hGroup);

            // Group for Vertical Axis
            GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

            vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(quantityLabel)
                    .addComponent(listQuantity));
            vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(priceLabel)
                    .addComponent(listPrice));


            vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(relistBtn));
            vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(messaging));
            layout.setVerticalGroup(vGroup);

            displayPanel.add(relistBtn);
            return displayPanel;
        }


        private class ButtonListener implements ActionListener {
            /**
             * When an action is performed
             */

            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton) e.getSource();

                if (source == relistBtn) {
                    relistAsset();
                }
            }

            /**
             * Relist the Sell Offer with a new quantity and/or price
             * A different trade ID will be issued however
             */
            private void relistAsset() {

                // Get user input for price and quantity
                String quantityString = listQuantity.getText();
                String priceString = listPrice.getText();
                try {
                    GUI.checkInputEmpty(quantityString);
                    GUI.checkInputEmpty(priceString);
                    int quantity = Integer.parseInt(quantityString);
                    double price = Double.parseDouble(priceString);
                    int quantityAvailable = loggedInMember.getQuantityAsset(data, selectedAssetTableOne);
                    if (quantity < quantityAvailable) {
                        SellOffer oldOffer = SellOfferData.getInstance().getOffer(listingID);
                        SellOfferData.getInstance().removeOffer(listingID);
                        SellOffer relist = new SellOffer(oldOffer.getAssetName(), quantity, price,
                                oldOffer.getUsername(), oldOffer.getUnitName());
                        SellOfferData.getInstance().addSellOffer(relist);
                        int resolveStatus = loggedInMember.listSellOrder(oldOffer.getAssetName(), quantity,price);
                        JOptionPane.showMessageDialog(null,
                                "Successfully relisted offer: " + oldOffer.getAssetName() + " quantity: " + quantity + " price: " + price);
                        Helper.displayNotification(resolveStatus);
                        updateTables();
                        updateMemberTextDisplay();
                        dispose();

                    }
                    else {
                        messaging.setText("Insufficient assets to list for selling.");
                    }
                } catch (EmptyFieldException e) {
                    messaging.setText("Price/ Quantity cannot be empty");
                } catch (DatabaseException e) {
                    e.printStackTrace();
                } catch (NumberFormatException errorMessage) {
                    messaging.setText("Please enter a number value");
                }
            }
        }

        public class ClosingListener extends WindowAdapter {
            public void windowClosing(WindowEvent e) { dispose(); }
        }
    }
}
