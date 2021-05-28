package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.AssetTrading.BuyOffer;
import ElectronicAssetTradingPlatform.AssetTrading.BuyOfferData;
import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
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

public class BuyTabGUI extends JPanel {
    // Global variables
    private OrganisationalUnitMembers loggedInMember;
    private NetworkDataSource data;

    TreeMap<Integer, BuyOffer> buyOffers;


    public BuyTabGUI(OrganisationalUnitMembers member, NetworkDataSource dataSource) {
        loggedInMember = member;
        data = dataSource;

        memberTextDisplay();

        getUnitBuyOffers();
        System.out.println(buyOffers.size());
    }

    private void memberTextDisplay() {
        this.add(new JTextArea("welcome " + loggedInMember.getUsername()));
        // TODO : Add Unit Credits to string
        try {
            float credits = data.getCredits(loggedInMember.getUnitName());
            System.out.println(credits);
            this.add(new JTextArea(loggedInMember.getUnitName() + ": " + Float.toString(credits)));
        }

        catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private TreeMap<Integer, BuyOffer> getUnitBuyOffers() {
        buyOffers = BuyOfferData.getInstance().getOrgOffersMap(loggedInMember.getUnitName());
        return buyOffers;

    }






}
