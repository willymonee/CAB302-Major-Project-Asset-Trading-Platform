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
        this.add(unitBuyOffersTable());

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
        buyOffers = null;
        buyOffers = BuyOfferData.getInstance().getOrgOffersMap(loggedInMember.getUnitName());
        return buyOffers;

    }

    // TODO : REMOVE ORG UNIT AS A COLUMN, AND ISNTEAD HAVE A BUTTON TO EDIT THE ASSET OFFER LISTING
    private JTable unitBuyOffersTable() {
        //ArrayList<String> dataArrayList = getRowData();
        String data[][] = { {"101","Amit","670000", "a", "a", "a"},
                            {"101","Sachin","700000", "a", "a", "a"}};

        // covnert to String[]
        //String[] dataRows = new String[dataArrayList.size()];
        //dataRows = dataArrayList.toArray(dataRows);


        String columns[] = { "Offer ID", "Asset Name", "Quantity", "Price per unit", "Offer Creator", "Org Unit"};
        JTable buyOffersTable = new JTable(data, columns);

        return buyOffersTable;
    }

    // makle return type whatever data is
    private String[][] getRowData() {
        String[][] data = new String[buyOffers.size()][6];
        buyOffers = getUnitBuyOffers();
        int count = 0;
        for(Map.Entry<Integer, BuyOffer> entry : buyOffers.entrySet()) {
            Integer key = entry.getKey();
            BuyOffer value = entry.getValue();
            //data.add(key, value.toString());
            //data[count][] = buyOffers.get(key);





            System.out.println(entry.getValue());
            count++;
        }
        return data;

    }






}
