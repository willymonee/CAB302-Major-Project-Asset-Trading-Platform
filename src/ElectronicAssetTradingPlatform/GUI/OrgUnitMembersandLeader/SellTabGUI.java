package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import javax.swing.*;

public class SellTabGUI extends JPanel {

    public SellTabGUI(OrganisationalUnitMembers member) {
        this.add(new JTextField("Sell Card", 10));
    }
}
