package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.AssetTrading.BuyOffer;
import ElectronicAssetTradingPlatform.AssetTrading.BuyOfferData;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

public class BuyTabGUI extends JPanel{
    // Global variables
    private JButton btn1;

    private OrganisationalUnitMembers loggedInMember;

    // Parameters would be network and user
    public BuyTabGUI(OrganisationalUnitMembers member) {
        loggedInMember = member;

        memberTextDisplay();

    }

    private void memberTextDisplay() {
        this.add(new JTextArea("welcome " + loggedInMember.getUsername()));
        // TODO : Add Unit Credits to string
        this.add(new JTextArea(loggedInMember.getUnitName()));
    }

    private TreeMap<Integer, BuyOffer> getUnitBuyOffers() {

        return BuyOfferData.getInstance().getOrgOffersMap(loggedInMember.getUnitName());
    }






}
