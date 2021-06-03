package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class OrgUnitMemGUI extends JFrame implements ChangeListener {

    public static final int WIDTH = 900;
    public static final int HEIGHT = 400;
    final static int extraWindowWidth = 100;

    private final static String buyTab = "BUY";
    private final static String sellTab = "SELL";
    private final static String orgUnitTab = "ORG UNIT";
    private final static String accountTab = "ACCOUNT";

    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();


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
        SellTabGUI sellCard = new SellTabGUI(loggedInUser,data);
        OrgUnitTabGUI orgUnitCard = new OrgUnitTabGUI(loggedInUser, data);
        AccountTabGUI accountCard = new AccountTabGUI(loggedInUser, data);
        menuTabs.add("Buy", buyCard);
        menuTabs.add("Sell", sellCard);
        menuTabs.add("Org Unit", orgUnitCard);
        menuTabs.add("Account", accountCard);
        menuTabs.setFont(new Font ( "Dialog", Font.BOLD, 24));
        menuTabs.setBackground(Color.WHITE);
        menuTabs.addChangeListener(this);
        // assign change listener's as each tab
        addChangeListener(buyCard);
        addChangeListener(sellCard);
        addChangeListener(orgUnitCard);

        return menuTabs;
    }

    // State change when switching JTabs
    // https://stackoverflow.com/questions/16427335/refresh-jpanel-content-on-tab-switch by MadProgrammer
    /**
     * When a new tab is selected e.g. switching from the buy tab to sell tab
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        fireStateChanged();
    }

    /**
     * Add a change listener
     * @param listener an element which listens for changes, in this case it is the tabs in the org unit mem GUI
     */
    public void addChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * Send notification/state changed event to listener's that the tab has been changed
     */
    protected void fireStateChanged() {
        ListIterator<ChangeListener> iter = listeners.listIterator();
        ChangeEvent evt = new ChangeEvent(this);
        while (iter.hasNext()) {
            iter.next().stateChanged(evt);
        }
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
