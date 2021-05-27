package ElectronicAssetTradingPlatform.GUI;

import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;
import ElectronicAssetTradingPlatform.Users.User;
import ElectronicAssetTradingPlatform.Users.UsersFactory;


import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class OrgUnitMemGUI extends JFrame {

    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;

    OrganisationalUnitMembers loggedInUser;
    NetworkDataSource data;

    private JButton goToBuyMenu;
    private JButton goToSellMenu;
    private JButton goToOrgUnitMenu;
    private JButton goToAccountMenu;

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
        goToBuyMenu.addActionListener(new ButtonListener());
        goToSellMenu.addActionListener(new ButtonListener());
        goToOrgUnitMenu.addActionListener(new ButtonListener());
        goToAccountMenu.addActionListener(new ButtonListener());


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

    private JPanel buttonPanel() {
        JPanel displayPanel = new JPanel();
        FlowLayout buttonLayout = new FlowLayout();
        displayPanel.setLayout(buttonLayout);


        // Create the Buttons that are to be placed in the Panel

        goToBuyMenu = new JButton("BUY");
        goToSellMenu = new JButton("SELL");
        goToOrgUnitMenu = new JButton("ORG UNIT");
        goToAccountMenu = new JButton("ACCOUNT");



        displayPanel.add(goToBuyMenu);
        displayPanel.add(goToSellMenu);
        displayPanel.add(goToOrgUnitMenu);
        displayPanel.add(goToAccountMenu);

        // Default landing page
        goToBuyMenu.setEnabled(true);

        return displayPanel;
    }

    private class ClosingListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) { dispose(); }
    }

    private class ButtonListener implements ActionListener {


        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (goToBuyMenu.equals(source)) {
                System.out.println("Buy");
            }
            else if (goToSellMenu.equals(source)) {
                System.out.println("Sell");
            }

            else if (goToOrgUnitMenu.equals(source)) {
                System.out.println("Org Unit");
            }

            else if (goToAccountMenu.equals(source)) {
                System.out.println("account");
            }

        }
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
