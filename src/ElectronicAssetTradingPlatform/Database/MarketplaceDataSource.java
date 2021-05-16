package ElectronicAssetTradingPlatform.Database;

import ElectronicAssetTradingPlatform.AssetTrading.Asset;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;
import ElectronicAssetTradingPlatform.Users.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


/**
 * Class to handle the Marketplace table in the database, such as insertions, deletions and querying
 */
public class MarketplaceDataSource {
    private static final String INSERT_BUYOFFER = "INSERT INTO Marketplace (Buy_or_Sell, "
            + "Unit_ID, User_ID, Asset_type_ID, Price_per_unit)"
            + "VALUES (?, ?, ?, ?, ?);";
    private static final String INSERT_SELLOFFER = "INSERT INTO Marketplace (Offer_ID, Buy_or_Sell, "
            + "Unit_ID, User_ID, Asset_type_ID, Price_per_unit)"
            + "VALUES (?, ?, ?, ?, ?, ?);";
    private static final String GET_OFFERS = "SELECT * FROM Marketplace WHERE Buy_or_Sell= ?";
    private static final String RESOLVE_OFFER = "DELETE FROM Marketplace WHERE Offer_ID=?";

    private PreparedStatement insertBuyOffer;
    private PreparedStatement insertSellOffer;
    private PreparedStatement getOffers;
    private PreparedStatement resolveOffer;

    private Connection connection;

    public MarketplaceDataSource() {
        connection = DBConnectivity.getInstance();
        try {
            insertBuyOffer = connection.prepareStatement(INSERT_BUYOFFER);
            insertSellOffer = connection.prepareStatement(INSERT_SELLOFFER);
            getOffers = connection.prepareStatement(GET_OFFERS);
            resolveOffer = connection.prepareStatement(RESOLVE_OFFER);
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    // Maybe a parameter will not be Asset asset, if it can be achievied via db
    public void insertBuyOffer(User user, Asset asset, String assetPrice) {
        try {
            insertBuyOffer.setString(1, "b");
            // Get Unit ID
            // switch case for org unit mem, leader and whoever else can create a buy offer
            if (user.getClass() == OrganisationalUnitMembers.class) {
                UnitDataSource unitDB = new UnitDataSource();
                System.out.println("test");
                String unitName = ((OrganisationalUnitMembers) user).getUnitName();
                System.out.println(unitName);
                String unitID = unitDB.executeGetUnitID(unitName);
                System.out.println(unitID);
                insertBuyOffer.setString(2, unitID);
                String userID = unitDB.executeGetUserID(user.getUsername());
                System.out.println(userID);
                insertBuyOffer.setString(3, unitID);
            }

            /*
            if (user.getClass() == OrganisationalUnitMembers.class) {
                UnitDataSource unitDB = new UnitDataSource();
                String userID = unitDB.executeGetUserID(user.getUsername());
                System.out.println(userID);
                insertBuyOffer.setString(4, userID);
            }

             */

            /*
            else {

            }
             */
            insertBuyOffer.setString(4, asset.getAssetName());
            insertBuyOffer.setString(5, assetPrice);
            insertBuyOffer.execute();
            insertBuyOffer.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertSellOffer(User user, Asset asset, String assetPrice) {
        try {
            insertBuyOffer.setString(1, "null");
            insertBuyOffer.setString(2, "s");
            // Get Unit ID
            // switch case for org unit mem, leader and whoever else can create a buy offer
            if (user.getClass() == OrganisationalUnitMembers.class) {
                UnitDataSource unitDB = new UnitDataSource();
                String unitID = unitDB.executeGetUnitID(((OrganisationalUnitMembers) user).getUnitName());
                insertBuyOffer.setString(3, unitID);
            }

            else {
                //
            }
            if (user.getClass() == OrganisationalUnitMembers.class) {
                UnitDataSource unitDB = new UnitDataSource();
                String userID = unitDB.executeGetUserID(((OrganisationalUnitMembers) user).getUnitName());
                insertBuyOffer.setString(4, userID);
            }
            insertBuyOffer.setString(5, asset.getAssetName());
            insertBuyOffer.setString(6, assetPrice);
            insertBuyOffer.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // maybe a param can be unit credits BUY/SELL get offers can be merged into 1 method
    public HashMap<String, String> getBuyOffers(User user) {
        HashMap<String, String> buyOffers = new HashMap<String, String>();
        ResultSet rs = null;

        try {
            //getBuyOffers.setString(1, "b");
            rs = getOffers.executeQuery();
            while(rs.next()) {
                buyOffers.put(rs.getString("Asset_type_ID"), rs.getString("Price_per_unit"));
            }
            rs.close();
            // maybe stuff about querying user
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buyOffers;
    }

    // maybe a param can be unit credits
    public HashMap<String, String> getSellOffers(User user) {
        HashMap<String, String> sellOffers = new HashMap<String, String>();
        ResultSet rs = null;

        try {
            rs = getOffers.executeQuery();
            while(rs.next()) {
                sellOffers.put(rs.getString("Asset_type_ID"), rs.getString("Price_per_unit"));
            }
            rs.close();
            // maybe stuff about querying user
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sellOffers;
    }

    /* maybe params r unit id, user id, ppu
    public void resolveOffer(User user, Asset asset) {
        try {
            // get the offer id from marketplace
            // resolveOffer.setString("1", )
        }
    }
    */



}
