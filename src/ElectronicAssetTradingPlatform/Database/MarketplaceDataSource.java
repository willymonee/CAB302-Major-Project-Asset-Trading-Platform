package ElectronicAssetTradingPlatform.Database;

import ElectronicAssetTradingPlatform.AssetTrading.Asset;
import ElectronicAssetTradingPlatform.AssetTrading.BuyOffer;
import ElectronicAssetTradingPlatform.AssetTrading.SellOffer;
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
            + "Unit_ID, User_ID, Asset_type_ID, Price_per_unit, Quantity)"
            + "VALUES (?, ?, ?, ?, ?, ?);";
    private static final String INSERT_SELLOFFER = "INSERT INTO Marketplace (Buy_or_Sell, "
            + "Unit_ID, User_ID, Asset_type_ID, Price_per_unit, Quantity)"
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

    // Maybe a parameter will not be Asset asset, if it can be achieved via db
    public void insertBuyOffer(BuyOffer buyOffer) {
        try {
            insertBuyOffer.setString(1, "b");
            UnitDataSource unitDB = new UnitDataSource();
            String unitID = unitDB.executeGetUnitID(buyOffer.getUnitName());
            insertBuyOffer.setString(2, unitID);
            String userID = unitDB.executeGetUserID(buyOffer.getUsername());
            insertBuyOffer.setString(3, userID);
            insertBuyOffer.setString(4, buyOffer.getAssetName());
            insertBuyOffer.setString(5, String.valueOf(buyOffer.getPricePerUnit()));
            insertBuyOffer.setString(6, String.valueOf(buyOffer.getQuantity()));
            insertBuyOffer.execute();
            insertBuyOffer.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertSellOffer(SellOffer sellOffer) {
        try {
            insertSellOffer.setString(1, "s");
            UnitDataSource unitDB = new UnitDataSource();
            String unitID = unitDB.executeGetUnitID(sellOffer.getUnitName());
            insertSellOffer.setString(2, unitID);
            String userID = unitDB.executeGetUserID(sellOffer.getUsername());
            insertSellOffer.setString(3, userID);
            insertSellOffer.setString(4, sellOffer.getAssetName());
            insertSellOffer.setString(5, String.valueOf(sellOffer.getPricePerUnit()));
            insertSellOffer.setString(6, String.valueOf(sellOffer.getQuantity()));
            insertSellOffer.execute();
            insertSellOffer.close();
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

    public void close() throws SQLException {
        connection.close();
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
