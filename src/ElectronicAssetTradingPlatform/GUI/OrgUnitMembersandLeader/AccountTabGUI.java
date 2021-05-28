package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import javax.swing.*;

public class AccountTabGUI extends JPanel{

    public AccountTabGUI(OrganisationalUnitMembers member) {
        this.add(new JTextField("Account Card", 10));
    }


}
