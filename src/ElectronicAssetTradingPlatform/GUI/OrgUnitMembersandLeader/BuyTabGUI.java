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
import java.util.ArrayList;
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
        getRowData();
        //this.add(unitBuyOffersTable());
        JTable buyOffersTable = unitBuyOffersTable();
        JScrollPane scrollPane = new JScrollPane(buyOffersTable);
        this.add(scrollPane);
    // TODO: BUTTON COLUMN TO EDIT ASSET LISTING https://camposha.info/java-jtable-buttoncolumn-tutorial/
    }

    private void memberTextDisplay() {
        this.add(new JTextArea("welcome " + loggedInMember.getUsername()));
        try {
            float credits = data.getCredits(loggedInMember.getUnitName());
            System.out.println(credits);
            this.add(new JTextArea(loggedInMember.getUnitName() + ": " + Float.toString(credits)));
        }

        catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private TreeMap<Integer, BuyOffer> getUnitBuyOffers() throws Exception {
        buyOffers = null;
        buyOffers = BuyOfferData.getInstance().getOrgOffersMap(loggedInMember.getUnitName());

        if (buyOffers == null) {
            throw new Exception();
        }

        return buyOffers;

    }

    // TODO : REMOVE ORG UNIT AS A COLUMN, AND ISNTEAD HAVE A BUTTON TO EDIT THE ASSET OFFER LISTING
    private JTable unitBuyOffersTable() {
        String data[][] = getRowData();

        String columns[] = { "Offer ID", "Asset Name", "Quantity", "Price per unit", "Offer Creator", "Org Unit"};
        JTable buyOffersTable = new JTable(data, columns);
        buyOffersTable.setBounds(30,40,200,300);
        return buyOffersTable;
    }

    private String[][] getRowData() {
        try {
            buyOffers = getUnitBuyOffers();
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        String[][] data = new String[buyOffers.size()][];
        int count = 0;
        for(Map.Entry<Integer, BuyOffer> entry : buyOffers.entrySet()) {
            //Integer key = entry.getKey();
            BuyOffer value = entry.getValue();


            data[count] = new String[] {
                String.valueOf(value.getOfferID()),
                value.getAssetName(),
                String.valueOf(value.getQuantity()),
                String.valueOf(value.getPricePerUnit()),
                value.getUsername(),
                value.getUnitName()
            };
            count++;
        }
        return data;
    }

}
