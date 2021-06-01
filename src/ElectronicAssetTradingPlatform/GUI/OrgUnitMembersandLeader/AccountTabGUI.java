package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.GUI.ChangePasswordGUI;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountTabGUI extends JPanel{

    OrganisationalUnitMembers loggedInUser;
    NetworkDataSource data;

    private JButton openChangePassMenu;
    private JLabel usernameLabel;

    /**
     * Constructor (a JPanel) for the Account Tab in the Member GUI
     * @param member    the member accessing the GUI
     * @param net       the network connection
     */
    public AccountTabGUI(OrganisationalUnitMembers member, NetworkDataSource net) {
        loggedInUser = member;
        data = net;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Greeting Text Label
        String greetUser = "Hello, " + member.getUsername();
        usernameLabel = Helper.createLabel(greetUser, 25);
        usernameLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(0,100,0,100));
        this.add(usernameLabel);

        // Button to open password changer menu
        openChangePassMenu = new JButton("Change password Menu");
        openChangePassMenu.addActionListener(new ButtonListener(member, data));
        //openChangePassMenu.setAlignmentX(Component.RIGHT_ALIGNMENT);
        //openChangePassMenu.setBorder(BorderFactory.createEmptyBorder(0,100,0,100));
        this.add(openChangePassMenu);

        // Page background colour
        this.setBackground(Color.WHITE);

    }

    /**
     * Handles button action/events
     */
    private class ButtonListener implements ActionListener {
        OrganisationalUnitMembers loggedInUser;
        NetworkDataSource data;

        /**
         * Constructor for the Button Listener
         * @param member    the member accessing the GUI and who pressed the button
         * @param net       the network connection
         */
        private ButtonListener(OrganisationalUnitMembers member, NetworkDataSource net) {
            loggedInUser = member;
            data = net;
        }

        /**
         * Action performed on button
         * @param e         the action event of the button (in this case it was clicked)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source == openChangePassMenu) {
                ChangePasswordGUI changePass = new ChangePasswordGUI(data, loggedInUser);
            }
        }
    }


}
