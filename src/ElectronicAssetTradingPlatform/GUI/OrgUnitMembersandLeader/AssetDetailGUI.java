package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.AssetTrading.*;
import ElectronicAssetTradingPlatform.Database.MarketplaceHistoryDataSource;
import ElectronicAssetTradingPlatform.Database.UnitDataSource;
import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;
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
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class AssetDetailGUI extends JFrame implements ActionListener {
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
    private JTextField quantityBuy;
    private JTextField priceBuy;
    private String assetName;
    private JPanel buyResponseMessage;

    public AssetDetailGUI(OrganisationalUnitMembers loggedInUser, NetworkDataSource data, Asset selectedAsset) {
        this.loggedInUser = loggedInUser;
        this.dataSource = data;
        this.selectedAsset = selectedAsset;
        assetName = selectedAsset.getAssetName();

        Container contentPane = this.getContentPane();
        contentPane.setPreferredSize(new Dimension(850, 850));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        contentPane.add(Box.createVerticalStrut(20));
        assetBuyOffersPanel = Helper.createPanel(Color.WHITE);
        assetBuyOffersTable = assetBuyOffersTable();
        assetBuyOffersScrollPane = Helper.createScrollPane(assetBuyOffersTable, assetBuyOffersPanel);
        assetBuyOffersPanel.add(Helper.createLabel(assetName + " Buy Offers", 20));
        assetBuyOffersPanel.add(assetBuyOffersScrollPane);
        sellButton = createButton("SELL");
        assetBuyOffersPanel.add(sellButton);

        assetSellOffersPanel = Helper.createPanel(Color.WHITE);
        assetSellOffersTable = assetSellOffersTable();
        assetSellOffersScrollPane = Helper.createScrollPane(assetSellOffersTable, assetSellOffersPanel);
        assetSellOffersPanel.add(Helper.createLabel(assetName + " Sell Offers",20));
        assetSellOffersPanel.add(assetSellOffersScrollPane);
        buyButton = createButton("BUY");
        assetSellOffersPanel.add(buyButton);

        contentPane.add(assetBuyOffersPanel);
        contentPane.add(assetSellOffersPanel);

        contentPane.add(makeMarketPlaceHistoryGraph());

        // Decorate the frame and make it visible
        setTitle("Asset Detail");
        setMinimumSize(new Dimension(850, 400));
        pack();
        setVisible(true);
    }

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

    private JTable assetBuyOffersTable() {
        // create a table
        String data[][] = getAssetBuyOffersRowData();
        String columns[] = { "Offer ID", "Quantity", "Price", "Offer Creator"};
        assetBuyOfferModel = new DefaultTableModel(data, columns);
        JTable table = new JTable(assetBuyOfferModel);
        Helper.formatTable(table);
        return table;
    }

    private JTable assetSellOffersTable() {
        // create a table
        String data[][] = getAssetSellOffersRowData();
        String columns[] = { "Offer ID", "Quantity", "Price", "Offer Creator"};
        assetSellOfferModel = new DefaultTableModel(data, columns);
        JTable table = new JTable(assetSellOfferModel);
        Helper.formatTable(table);
        return table;
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
        if (src == this.buyButton) {
           displayBuyAssetPanel();
        }
        else if (src == this.sellButton) {
            System.out.println("pressed sell button");
        }

    }



    // https://stackoverflow.com/questions/41904362/multiple-joptionpane-input-dialogs by: Frakcool
    public void displayBuyAssetPanel() {
        buyAssetPanel = Helper.createPanel(Color.WHITE);
        buyAssetPanel.setLayout(new GridLayout(0, 2, 2, 2));

        quantityBuy = new JTextField(4);
        priceBuy = new JTextField(4);

        buyAssetPanel.add(new JLabel("Quantity to buy"));
        buyAssetPanel.add(quantityBuy);

        buyAssetPanel.add(new JLabel("Price"));
        buyAssetPanel.add(priceBuy);

        int option = JOptionPane.showConfirmDialog(null,
                buyAssetPanel,
                "Buy Asset" + selectedAsset.getAssetName(),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE, null);

        if (option == JOptionPane.OK_OPTION) {
            String quantity = quantityBuy.getText();
            String price = priceBuy.getText();
            try {
                int quantityInt = Integer.parseInt(quantity);
                int priceInt = Integer.parseInt(price);
                loggedInUser.listBuyOrderNoResolve(assetName, quantityInt, priceInt);
                JOptionPane.showMessageDialog(null,
                        "Successfully placed buy order for: " + assetName + " quantity: " + quantity + " price: " + price );
                this.dispose();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Please enter a valid number for quantity and price", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Warning",
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

            JLabel title = new JLabel("Asset Price History", SwingConstants.CENTER);
            panel.add(title, BorderLayout.NORTH);
            panel.add(graphContainer, BorderLayout.CENTER);
        } else {
            JLabel title = new JLabel("Asset Price History", SwingConstants.CENTER);
            panel.add(title, BorderLayout.NORTH);
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
