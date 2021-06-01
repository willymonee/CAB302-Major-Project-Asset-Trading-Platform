package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;


import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class OrgUnitMemGUI extends JFrame {

    public static final int WIDTH = 900;
    public static final int HEIGHT = 400;
    final static int extraWindowWidth = 100;

    private final static String buyTab = "BUY";
    private final static String sellTab = "SELL";
    private final static String orgUnitTab = "ORG UNIT";
    private final static String accountTab = "ACCOUNT";


    OrganisationalUnitMembers loggedInUser;
    NetworkDataSource data;


    /**
     * Creates Organisational Unit Member Main Menu
     * @param user the logged in Organisational Unit Member/Leader
     * @param dataSource the server connection
     */

    public OrgUnitMemGUI(OrganisationalUnitMembers user, NetworkDataSource dataSource) {
        data = dataSource;
        loggedInUser = user;
        initUI();

        // Button listeners
        addWindowListener(new ClosingListener());

        setTitle("Member/Leader Menu");
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        pack();
        setVisible(true);

    }

    /**
     * Initialise UI
     */
    private void initUI() {
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(tabMenuPane());
    }

    private JTabbedPane tabMenuPane() {
        JTabbedPane menuTabs = new JTabbedPane();

        BuyTabGUI buyCard = new BuyTabGUI(loggedInUser, data);
        SellTabGUI sellCard = new SellTabGUI(loggedInUser);
        OrgUnitTabGUI orgUnitCard = new OrgUnitTabGUI(loggedInUser, data);
        AccountTabGUI accountCard = new AccountTabGUI(loggedInUser, data);

        menuTabs.add("Buy", buyCard);
        menuTabs.add("Sell", sellCard);
        menuTabs.add("Org Unit", orgUnitCard);
        menuTabs.add("Account", accountCard);
        menuTabs.setFont(new Font ( "Dialog", Font.BOLD, 24));
        menuTabs.setBackground(Color.WHITE);


        return menuTabs;
    }

    private class ClosingListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) { dispose(); }
    }

    // TODO: remove
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //new OrgUnitMemGUI();
            }
        });
        JFrame.setDefaultLookAndFeelDecorated(true);
    }
}
