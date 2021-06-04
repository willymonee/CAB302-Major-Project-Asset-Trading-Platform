package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.AssetTrading.Asset;
import ElectronicAssetTradingPlatform.AssetTrading.BuyOffer;
import ElectronicAssetTradingPlatform.AssetTrading.BuyOfferData;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EditBuyOfferGUI extends JFrame {
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
     * Constructor for Editing a BuyOffer
     * @param data          The network connection
     * @param member        The logged in org unit member
     * @param asset         The specified asset to edit the listing for
     */

    // Maybe param is also previous quantity/ price
    public EditBuyOfferGUI(NetworkDataSource data, OrganisationalUnitMembers member, Asset asset,
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

        setTitle("Edit Buy Listing");
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
        JLabel titleLabel = Helper.createLabel("Edit Buy Order - " + asset.getAssetName(), 18);
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
        JLabel totalPriceLabel = Helper.createLabel("Total Price: ", 12);



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
                System.out.println("button relist was pressed: call function here"); // TODO DELETE THIS
                relistAsset();
            }
        }

        /**
         * Relist the Buy Offer with a new quantity and/or price
         * A different trade ID will be issued however
         */
        private void relistAsset() {
            // Get user input for price and quantity
            String quantityString = listQuantity.getText();
            int quantity = Integer.valueOf(quantityString);
            String priceString = listPrice.getText();
            double price = Double.parseDouble(priceString);

            // Error handling


            // Duplicate oldOffer in order to create New relisted offer
            BuyOffer oldOffer = BuyOfferData.getInstance().getOffer(listingID);
            BuyOfferData.getInstance().removeOffer(listingID);
            BuyOffer relist = new BuyOffer(oldOffer.getAssetName(), quantity, price,
                    oldOffer.getUsername(), oldOffer.getUnitName());
            System.out.println(relist);
            BuyOfferData.getInstance().addOffer(relist);
            dispose();



        }



        private BuyOffer getOldOffer(int listingID) {
            BuyOffer oldOffer = BuyOfferData.getInstance().getOffer(listingID);
            return oldOffer;
        }
    }

    private class ClosingListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) { dispose(); }
    }
}

