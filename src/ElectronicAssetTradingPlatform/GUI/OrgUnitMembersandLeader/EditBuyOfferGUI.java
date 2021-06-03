package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.AssetTrading.Asset;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EditBuyOfferGUI extends JFrame {
    private OrganisationalUnitMembers loggedInMember;
    private NetworkDataSource net;
    private Asset asset;

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
    public EditBuyOfferGUI(NetworkDataSource data, OrganisationalUnitMembers member, Asset asset) {
        net = data;
        loggedInMember = member;
        this.asset = asset;

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


        // TODO : add current quantity and price in JTextArea for respective comp
        JLabel quantityLabel = Helper.createLabel("Quantity:", 12);
        JLabel priceLabel = Helper.createLabel("Price:", 12);
        JLabel totalPriceLabel = Helper.createLabel("Total Price: ", 12);
        // TODO: Add asset price = quantity * price
        JLabel displayPrice = Helper.createLabel("", 10);
        // TODO: Add current order listing price information
        //JLabel currentOrdersInfo = Helper.createLabel("Current sell orders : ", 9);
        //JLabel currentOrdersInfoTwo = Helper.createLabel("x for sale starting at x credits or higher ", 7);


        listQuantity = new JTextField(10);
        listPrice = new JTextField(10);
        messaging = new JTextArea();
        messaging.setEditable(false);
        messaging.setLineWrap(true);
        messaging.setWrapStyleWord(true);

        relistBtn = new JButton("RELIST");

        // Group for Horizontal Axis
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(quantityLabel)
                .addComponent(priceLabel)
                .addComponent(totalPriceLabel));

        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(listQuantity)
                .addComponent(listPrice)
                .addComponent(totalPriceLabel)
                .addComponent(displayPrice)
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
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE))
                .addComponent(totalPriceLabel)
                .addComponent(displayPrice);

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
                System.out.println("button relist was pressed: call function here");
                // relistAsset();
            }
        }

        private void relistAsset() {
            String quantity = listQuantity.getText();
            String price = listPrice.getText();

            // functionality
        }
    }

    private class ClosingListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) { dispose(); }
    }
}

