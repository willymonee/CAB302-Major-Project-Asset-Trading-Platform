package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.GUI.ChangePasswordGUI;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountTabGUI extends JPanel{

    OrganisationalUnitMembers loggedInUser;
    NetworkDataSource data;

    private JButton openChangePassMenu;

    public AccountTabGUI(OrganisationalUnitMembers member, NetworkDataSource net) {
        loggedInUser = member;
        data = net;
        openChangePassMenu = new JButton("Change password Menu");
        openChangePassMenu.addActionListener(new ButtonListener(member, data));
        this.add(openChangePassMenu);
    }

    private class ButtonListener implements ActionListener {
        OrganisationalUnitMembers loggedInUser;
        NetworkDataSource data;
        private ButtonListener(OrganisationalUnitMembers member, NetworkDataSource net) {
            loggedInUser = member;
            data = net;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source == openChangePassMenu) {
                ChangePasswordGUI changePass = new ChangePasswordGUI(data, loggedInUser);
            }
        }
    }


}
