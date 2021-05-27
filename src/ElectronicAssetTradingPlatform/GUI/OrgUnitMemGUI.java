package ElectronicAssetTradingPlatform.GUI;

import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;
import ElectronicAssetTradingPlatform.Users.User;
import ElectronicAssetTradingPlatform.Users.UsersFactory;


import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class OrgUnitMemGUI extends JFrame {

    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;
    final static int extraWindowWidth = 100;

    private final static String buyTab = "BUY";
    private final static String sellTab = "SELL";
    private final static String orgUnitTab = "ORG UNIT";
    private final static String accountTab = "ACCOUNT";


    OrganisationalUnitMembers loggedInUser;
    NetworkDataSource data;

    private JTabbedPane goToBuyMenu;
    private JTabbedPane goToSellMenu;
    private JTabbedPane goToOrgUnitMenu;
    private JTabbedPane goToAccountMenu;

    /**
     * Creates Organisational Unit Member Main Menu
     * @param user the logged in Organisational Unit Member/Leader
     * @param dataSource the server connection
     */

    // params OrganisationalUnitMembers user, NetworkDataSource dataSource
    public OrgUnitMemGUI() {
        //data = dataSource;
        //loggedInUser = user;

        initUI();

        // Button listeners
        addWindowListener(new ClosingListener());
        /*
        goToBuyMenu.addChangeListener();
        goToSellMenu.addChangeListener();
        goToOrgUnitMenu.addChangeListener();
        goToAccountMenu.addChangeListener();
        */



        setTitle("Test Title");
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        pack();
        setVisible(true);


        //goToBuyMenu.addActionListener();
    }

    /**
     * Initialise UI
     */
    private void initUI() {
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        contentPane.add(Box.createVerticalStrut(20));
       // //contentPane.add(goToButtonsPanel());
        contentPane.add(buttonPanel());
    }

    private JTabbedPane buttonPanel() {
        JTabbedPane menuTabs = new JTabbedPane();
        //CardLayout buttonLayout = new CardLayout();
        //displayPanel.setLayout(buttonLayout);
        JPanel buyCard = new JPanel() {

            public Dimension getSize() {
                Dimension size = super.getSize();
                size.width += extraWindowWidth;
                return size;
            }

            public void test() {
                System.out.println("buy card");
            }
        };

        JPanel sellCard = new JPanel();
        JPanel orgUnitCard = new JPanel();
        JPanel accountCard = new JPanel();

        menuTabs.add("BUY", buyCard);
        menuTabs.add("SELL", sellCard);
        menuTabs.add("ORG UNIT", orgUnitCard);
        menuTabs.add("ACCOUNT", accountCard);


        return menuTabs;
    }

    private class ClosingListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) { dispose(); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new OrgUnitMemGUI();
            }
        });
        JFrame.setDefaultLookAndFeelDecorated(true);
        //FramesAndPanels swing = new FramesAndPanels("BorderLayout");
        //swing.createGUI();

    }
}
