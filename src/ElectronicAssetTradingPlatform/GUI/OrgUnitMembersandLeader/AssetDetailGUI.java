package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.AssetTrading.*;
import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;
import ElectronicAssetTradingPlatform.Exceptions.InsufficientAssetsException;
import ElectronicAssetTradingPlatform.Exceptions.InsufficientCreditsException;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.util.*;
import java.util.List;

/**
 * AssetDetailGUI is opened whenever an asset is selected and the view asset button is pressed
 * It displays buy and sell offers for that particular asset, allows the user to make buy and sell offers for that asset
 * and also displays a price history chart for it
 */
public class AssetDetailGUI extends JFrame implements ActionListener {
    // global variable
    private final OrganisationalUnitMembers loggedInUser;
    private final NetworkDataSource dataSource;
    private final Asset selectedAsset;
    private JTable assetBuyOffersTable;
    private JTable assetSellOffersTable;
    private TableModel assetBuyOfferModel;
    private TableModel assetSellOfferModel;
    private JPanel assetBuyOffersPanel;
    private JScrollPane assetBuyOffersScrollPane;
    private JPanel assetSellOffersPanel;
    private JScrollPane assetSellOffersScrollPane;
    private JButton buyButton;
    private JButton sellButton;
    private JPanel buyAssetPanel;
    private JPanel sellAssetPanel;
    private JTextField quantityBuyField;
    private JTextField priceBuyField;
    private String assetName;
    private JTextField quantitySellField;
    private JTextField priceSellField;
    private JPanel assetsCreditsOwnedPanel;
    private double creditsAvailable;
    private int quantityAvailable;

    /**
     * Constructor for the GUI
     * @param loggedInUser - user who is logged in
     * @param data - network data source
     * @param selectedAsset - asset that is being displayed
     */
    public AssetDetailGUI(OrganisationalUnitMembers loggedInUser, NetworkDataSource data, Asset selectedAsset) {
        this.loggedInUser = loggedInUser;
        this.dataSource = data;
        this.selectedAsset = selectedAsset;
        assetName = selectedAsset.getAssetName();
        // set the container
        Container contentPane = new Container();
        contentPane.setPreferredSize(new Dimension(850, 1100));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(Box.createVerticalStrut(20));
        // display the assets and credits available to be used for sell and buy offers
        JLabel assetsOwned = Helper.createLabel(assetsAvailableToString(), 16);
        JLabel creditsAvailable = Helper.createLabel(creditsAvailableToString(), 16);
        assetsCreditsOwnedPanel = Helper.createPanel(Color.WHITE);
        assetsCreditsOwnedPanel.setLayout(new BorderLayout());
        assetsCreditsOwnedPanel.add(assetsOwned, BorderLayout.WEST);
        assetsCreditsOwnedPanel.add(creditsAvailable, BorderLayout.EAST);
        contentPane.add(assetsCreditsOwnedPanel);
        // display the asset's buy offers - with a sell button to list a corresponding sell offer
        assetBuyOffersPanel = Helper.createPanel(Color.WHITE);
        assetBuyOffersTable = assetBuyOffersTable();
        assetBuyOffersScrollPane = Helper.createScrollPane(assetBuyOffersTable, assetBuyOffersPanel);
        assetBuyOffersPanel.add(Helper.createLabel(assetName + " Buy Offers", 20));
        assetBuyOffersPanel.add(assetBuyOffersScrollPane);
        assetBuyOffersPanel.add(Helper.createLabel("Current buy offers: " + getBuyQuantity()
                + " requests to buy at " + getBestBuyPrice() + " credits or lower", 14));
        sellButton = createButton("SELL");
        assetBuyOffersPanel.add(sellButton);
        // display the asset's sell offers - with a buy button to list a corresponding buy offer
        assetSellOffersPanel = Helper.createPanel(Color.WHITE);
        assetSellOffersTable = assetSellOffersTable();
        assetSellOffersScrollPane = Helper.createScrollPane(assetSellOffersTable, assetSellOffersPanel);
        assetSellOffersPanel.add(Helper.createLabel(assetName + " Sell Offers",20));
        assetSellOffersPanel.add(assetSellOffersScrollPane);
        assetSellOffersPanel.add(Helper.createLabel("Current sell offers: " + getSellQuantity()
                + " for sale starting at " + getBestSellPrice() + " credits", 14));
        buyButton = createButton("BUY");
        assetSellOffersPanel.add(buyButton);
        contentPane.add(assetBuyOffersPanel);
        contentPane.add(assetSellOffersPanel);
        // add the market place history graph
        contentPane.add(makeMarketPlaceHistoryGraph());
        JScrollPane scrPane = new JScrollPane(contentPane);
        scrPane.setPreferredSize(new Dimension(900, 850));
        add(scrPane);
        // Decorate the frame and make it visible
        setTitle(assetName + " Details");
        setMinimumSize(new Dimension(850, 400));
        pack();
        setVisible(true);
        addWindowListener(new ClosingListener());
    }

    // retrieve the highest buy price for the asset
    private double getBestBuyPrice() {
        return BuyOfferData.getInstance().getHighestPrice(assetName);
    }

    // retrieve the quantity of buy offers for the asset
    private int getBuyQuantity() {
        return BuyOfferData.getInstance().assetQuantity(assetName);
    }

    // retrieve the lowest sell price for the asset
    private double getBestSellPrice() {
        return SellOfferData.getInstance().getLowestPricedSellOffer(assetName);
    }

    // retrieve the quantity of sell offers for the asset
    private int getSellQuantity() {
        return SellOfferData.getInstance().assetQuantity(assetName);
    }

    // return a string containing the amount of assets available for use in sell offers
    private String assetsAvailableToString() {
        String assetOwned = assetName + "s owned: ";
        int totalAssetOwned = getTotalAssetOwned(); // amount the org actually owns
        int quantityInSellOffers = getQuantityInSellOffers(); // amount that is already in current sell offers
        quantityAvailable = totalAssetOwned - quantityInSellOffers;
        assetOwned += quantityAvailable + " (" + totalAssetOwned + " - " + quantityInSellOffers + ")";
        return assetOwned;
    }

    // return the total amount of an asset the org owns
    private int getTotalAssetOwned() {
        try {
            return loggedInUser.getQuantityAsset(dataSource, assetName);
        } catch (DatabaseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            return 0;
        }
        return 0;
    }

    // return the total amount of an asset that the org has placed in current sell offers
    private int getQuantityInSellOffers() {
        return SellOfferData.getInstance().quantityAssetInSellOffer(loggedInUser.getUnitName(), assetName);
    }

    // return as a string the amount of credits available to be made for buy offesr
    private String creditsAvailableToString() {
        double totalCredits = getCredits();
        double creditsInBuyOffers = getCreditsInBuyOffers();
        creditsAvailable = totalCredits - creditsInBuyOffers;
        return "Credits Available: " + creditsAvailable + " (" + totalCredits + " - " + creditsInBuyOffers+ ")";
    }

    // return the amount of credits used in existing buy offers made by the org unit
    private double getCreditsInBuyOffers() {
        return BuyOfferData.getInstance().creditsInBuyOffers(loggedInUser.getUnitName());
    }

    // return the amount of credits the org actually owns
    private double getCredits() {
        try {
            return dataSource.getCredits(loggedInUser.getUnitName());
        } catch (DatabaseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            return 0;
        }
        return 0;
    }

    // retrieve buy offers for the asset as row data for the tables
    private String[][] getAssetBuyOffersRowData() {
        TreeMap<Integer, BuyOffer> assetBuyOffers =  BuyOfferData.getInstance().getAssetOffers(selectedAsset.getAssetName());
        String[][] data = new String[assetBuyOffers.size()][];
        int count = 0;
        for(Map.Entry<Integer, BuyOffer> entry : assetBuyOffers.entrySet()) {
            BuyOffer value = entry.getValue();
            data[count] = new String[] {
                    String.valueOf(value.getOfferID()),
                    String.valueOf(value.getQuantity()),
                    String.valueOf(value.getPricePerUnit()),
                    value.getUsername()
            };
            count++;
        }
        return data;
    }

    // retrieve sell offers for the asset as row data for the tables
    private String[][] getAssetSellOffersRowData() {
        TreeMap<Integer, SellOffer> assetSellOffers =  SellOfferData.getInstance().getAssetOffers(selectedAsset.getAssetName());
        String[][] data = new String[assetSellOffers.size()][];
        int count = 0;
        for(Map.Entry<Integer, SellOffer> entry : assetSellOffers.entrySet()) {
            SellOffer value = entry.getValue();
            data[count] = new String[] {
                    String.valueOf(value.getOfferID()),
                    String.valueOf(value.getQuantity()),
                    String.valueOf(value.getPricePerUnit()),
                    value.getUsername()
            };
            count++;
        }
        return data;
    }

    // create an buy offers table for the asset
    private JTable assetBuyOffersTable() {
        // create a table
        String[][] data = getAssetBuyOffersRowData();
        String[] columns = { "Offer ID", "Quantity", "Price", "Offer Creator"};
        assetBuyOfferModel = new DefaultTableModel(data, columns);
        JTable table = new JTable(assetBuyOfferModel);
        Helper.formatTable(table);
        return table;
    }

    // create a sell offers table for the asset
    private JTable assetSellOffersTable() {
        // create a table
        String[][] data = getAssetSellOffersRowData();
        String[] columns = { "Offer ID", "Quantity", "Price", "Offer Creator"};
        assetSellOfferModel = new DefaultTableModel(data, columns);
        JTable table = new JTable(assetSellOfferModel);
        Helper.formatTable(table);
        return table;
    }

    // create a button
    private JButton createButton(String buttonText) {
        // create a JButton object and store it in a local var
        JButton button = new JButton();
        // Set the button text to that passed in String buttonText
        button.setText(buttonText);
        button.addActionListener(this);
        // Return the JButton
        return button;
    }
    // action listener for created buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == this.buyButton) {
           displayBuyAssetPanel();
        }
        else if (src == this.sellButton) {
            System.out.println("pressed sell button");
            displaySellAssetPanel();
        }
    }

    // https://stackoverflow.com/questions/41904362/multiple-joptionpane-input-dialogs by: Frakcool
    // open a panel for buying the asset
    public void displayBuyAssetPanel() {
        buyAssetPanel = Helper.createPanel(Color.WHITE);
        buyAssetPanel.setLayout(new GridLayout(0, 2, 2, 2));
        // text fields and labels
        quantityBuyField = new JTextField(4);
        priceBuyField = new JTextField(4);
        buyAssetPanel.add(new JLabel("Quantity to buy"));
        buyAssetPanel.add(quantityBuyField);
        buyAssetPanel.add(new JLabel("Price"));
        buyAssetPanel.add(priceBuyField);
        // confirmation pane
        int option = JOptionPane.showConfirmDialog(null,
                buyAssetPanel,
                "Buy Asset" + selectedAsset.getAssetName(),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE, null);
        // if OK button selected attempt to place the offer for the inputted quantity and price
        if (option == JOptionPane.OK_OPTION) {
            String quantity = quantityBuyField.getText();
            String price = priceBuyField.getText();
            try {
                int quantityInt = Integer.parseInt(quantity);
                float priceFloat = Float.parseFloat(price);
                // if the total cost of the offer is greater than the credits available thrown an InsufficientCreditsException
                if ((float) quantityInt * priceFloat > creditsAvailable) {
                    throw new InsufficientCreditsException("Not enough credits to create buy offer");
                }
                // otherwise attempt to list the offer
                int resolveStatus = loggedInUser.listBuyOrder(assetName, quantityInt, priceFloat);
                // display a success message
                JOptionPane.showMessageDialog(null,
                        "Successfully placed buy order for: " + assetName + " quantity: " + quantity + " price: " + price );
                // display a notification of the resolve status of the offer
                Helper.displayNotification(resolveStatus);
                // close the AssetDetailGUI window
                this.dispose();
            } catch (NumberFormatException e) { // if no number was entered/format incorrect
                JOptionPane.showMessageDialog(null,
                        "Please enter a valid number for quantity and price", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            } catch (IllegalArgumentException | InsufficientCreditsException e) { // if a value less than 1 or insufficient credits
                JOptionPane.showMessageDialog(null, e.getMessage(), "Warning",
                        JOptionPane.WARNING_MESSAGE);
            } catch (Exception e) { // all other exceptions
                JOptionPane.showMessageDialog(null, "Failed to insert buy offer", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // https://stackoverflow.com/questions/41904362/multiple-joptionpane-input-dialogs by: Frakcool
    public void displaySellAssetPanel() {
        sellAssetPanel = Helper.createPanel(Color.WHITE);
        sellAssetPanel.setLayout(new GridLayout(0, 2, 3, 2));
        // text fields and labels
        quantitySellField = new JTextField(4);
        priceSellField = new JTextField(4);
        sellAssetPanel.add(Helper.createLabel(assetsAvailableToString(), 12));
        sellAssetPanel.add(Helper.createLabel("", 12));
        sellAssetPanel.add(new JLabel("Quantity to sell"));
        sellAssetPanel.add(quantitySellField);
        sellAssetPanel.add(new JLabel("Price"));
        sellAssetPanel.add(priceSellField);
        // confirmation dialog buttons
        int option = JOptionPane.showConfirmDialog(null,
                sellAssetPanel,
                "Sell Asset " + selectedAsset.getAssetName(),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE, null);
        // if pressed OK attempt to insert a sell offer given the text inputs
        if (option == JOptionPane.OK_OPTION) {
            String quantity = quantitySellField.getText();
            String price = priceSellField.getText();
            try {
                int quantityInt = Integer.parseInt(quantity);
                float priceFloat = Float.parseFloat(price);
                // if the quantity selected is greater than the quantity available for selling throw an insufficient assets exception
                if (quantityInt > quantityAvailable) {
                    throw new InsufficientAssetsException("Not enough assets to sell");
                }
                // otherwise continue attempting to list a sell offer
                int resolveStatus = loggedInUser.listSellOrder(assetName, quantityInt, priceFloat);
                // display a success message for placing offer
                JOptionPane.showMessageDialog(null,
                        "Successfully placed sell order for: " + assetName + " quantity: " + quantity + " price: " + price);
                // display a notification describing the resolve status of the offer
                Helper.displayNotification(resolveStatus);
                // close the AssetDetailGUI
                this.dispose();
            } catch (NumberFormatException e) { // if invalid format
                JOptionPane.showMessageDialog(null,
                        "Please enter a valid number for quantity and price", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            } catch (IllegalArgumentException | InsufficientAssetsException e) { // if input is less than one or insufficent assets
                JOptionPane.showMessageDialog(null, e.getMessage(), "Warning",
                        JOptionPane.WARNING_MESSAGE);
            } catch (Exception e) { // all other exceptions
                JOptionPane.showMessageDialog(null, "Failed to insert sell offer", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }



    private JPanel makeMarketPlaceHistoryGraph() {
        JPanel panel = new JPanel(new BorderLayout());

        List<List<Object>> data;
        // {Date date, float price}
        try {
            data = dataSource.getAssetHistory(selectedAsset.getAssetName());
        } catch (DatabaseException e) {
            data = new ArrayList<>();
        }

        JLabel title = Helper.createLabel("Asset Price History", 20);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title, BorderLayout.NORTH);
        if (data.size() > 1) {
            // Sort by date
            data.sort(Comparator.comparingLong(a -> ((Date) a.get(0)).getTime()));

            Object[][] dataArrayIn = new Object[data.size()][];
            int count = 0;
            for (List<Object> row : data) {
                dataArrayIn[count] = row.toArray();
                count++;
            }

            MarketplaceHistoryGraph graph = new MarketplaceHistoryGraph(dataArrayIn, 400, 300);

            JPanel graphContainer = new JPanel();
            graphContainer.add(graph);
            graphContainer.setBorder(new EmptyBorder(20, -10, 10, -10));

            panel.add(graphContainer, BorderLayout.CENTER);
        } else {
            JLabel text = new JLabel("Data not found");
            JPanel textContainer = new JPanel();
            textContainer.add(text);
            textContainer.setBorder(new EmptyBorder(20, -10, 10, -10));
            panel.add(textContainer, BorderLayout.CENTER);
        }

        return panel;
    }

    /**
     * Closes the window
     */
    private class ClosingListener extends WindowAdapter {
        public void windowClosed(WindowEvent e) { dispose(); }
        public void windowClosing(WindowEvent e) { dispose(); }
    }

    public static void main(String[] args) {
        // TODO: remove main done
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                NetworkDataSource net = new NetworkDataSource();
                net.start();
                new AssetDetailGUI(new OrganisationalUnitMembers("a", "a", "a", "a"), net, new Asset("iPhone 10"));
            }
        });
    }
}
