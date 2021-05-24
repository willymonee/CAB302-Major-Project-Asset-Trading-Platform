package ElectronicAssetTradingPlatform.Database;


import ElectronicAssetTradingPlatform.AssetTrading.BuyOffer;
import ElectronicAssetTradingPlatform.AssetTrading.SellOffer;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;


/**
 * Class to handle the Marketplace table in the database, such as insertions, deletions,querying and resolving trades
 */
public class MarketplaceDataSource {
    private static final String BUY_OFFER = "b";
    private static final String SELL_OFFER = "s";

    private static final String INSERT_BUYOFFER = "INSERT INTO Marketplace (Buy_or_Sell, "
            + "Unit_ID, User_ID, Asset_type_ID, Price_per_unit, Quantity)"
            + "VALUES (?, ?, ?, ?, ?, ?);";
    private static final String INSERT_SELLOFFER = "INSERT INTO Marketplace (Buy_or_Sell, "
            + "Unit_ID, User_ID, Asset_type_ID, Price_per_unit, Quantity)"
            + "VALUES (?, ?, ?, ?, ?, ?);";
    private static final String GET_OFFERS = "SELECT * FROM Marketplace WHERE Buy_or_Sell= ?";
    private static final String REMOVE_OFFER = "DELETE FROM Marketplace WHERE Offer_ID=?";
    private static final String UPDATE_OFFER_QUANTITY = "UPDATE Marketplace SET Quantity=? WHERE Offer_ID=?";
    private static final String GET_PLACED_OFFER_ID = "SELECT MAX(Offer_ID) FROM Marketplace";


    private PreparedStatement insertBuyOffer;
    private PreparedStatement insertSellOffer;
    private PreparedStatement getOffers;
    private PreparedStatement removeOffer;
    private PreparedStatement updateOfferQuantity;
    private PreparedStatement getPlacedOfferID;

    private Connection connection;

    // Singleton
    private static class MarketPlaceDataHolder {
        private final static MarketplaceDataSource INSTANCE = new MarketplaceDataSource();
    }

    // retrieve instance of
    public static MarketplaceDataSource getInstance() { return MarketPlaceDataHolder.INSTANCE; }

    private MarketplaceDataSource() {
        connection = DBConnectivity.getInstance();
        try {
            insertBuyOffer = connection.prepareStatement(INSERT_BUYOFFER);
            insertSellOffer = connection.prepareStatement(INSERT_SELLOFFER);
            getOffers = connection.prepareStatement(GET_OFFERS);
            removeOffer = connection.prepareStatement(REMOVE_OFFER);
            updateOfferQuantity = connection.prepareStatement(UPDATE_OFFER_QUANTITY);
            getPlacedOfferID = connection.prepareStatement(GET_PLACED_OFFER_ID);
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    /**
     * Remove an offer from the database
     * @param offerID of the offer to be removed
     */
    public void removeOffer(int offerID) {
        try {
            removeOffer.setInt(1, offerID);
            removeOffer.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Insert a buy offer into the database
     * @param buyOffer buy offer to be added
     */
    public void insertBuyOffer(BuyOffer buyOffer) {
        try {
            insertBuyOffer.setString(1, BUY_OFFER);
            UnitDataSource unitDB = new UnitDataSource();
            String unitID = unitDB.executeGetUnitID(buyOffer.getUnitName());
            insertBuyOffer.setString(2, unitID);
            String userID = unitDB.executeGetUserID(buyOffer.getUsername());
            insertBuyOffer.setString(3, userID);
            int assetID = unitDB.executeGetAssetID(buyOffer.getAssetName());
            insertBuyOffer.setInt(4, assetID);
            insertBuyOffer.setString(5, String.valueOf(buyOffer.getPricePerUnit()));
            insertBuyOffer.setString(6, String.valueOf(buyOffer.getQuantity()));
            insertBuyOffer.execute();
            //insertBuyOffer.close();
        } catch (SQLException e) {
            System.out.println("Cannot create buy offer for asset not in the system. " + e.getMessage());
            //e.printStackTrace();
        }
    }

    /**
     * Insert a sell offer into the database
     * @param sellOffer to be added
     */
    public void insertSellOffer(SellOffer sellOffer) {
        try {
            insertSellOffer.setString(1, SELL_OFFER);
            UnitDataSource unitDB = new UnitDataSource();
            String unitID = unitDB.executeGetUnitID(sellOffer.getUnitName());
            insertSellOffer.setString(2, unitID);
            String userID = unitDB.executeGetUserID(sellOffer.getUsername());
            insertSellOffer.setString(3, userID);
            int assetID = unitDB.executeGetAssetID(sellOffer.getAssetName());
            insertSellOffer.setInt(4, assetID);
            insertSellOffer.setString(5, String.valueOf(sellOffer.getPricePerUnit()));
            insertSellOffer.setString(6, String.valueOf(sellOffer.getQuantity()));
            insertSellOffer.execute();
            //insertSellOffer.close();
        } catch (SQLException e) {
            System.out.println("Cannot create sell offer for asset not in the system. " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Update the quantity of an offer
     * @param newQuantity quantity to set the offer
     * @param offerID of the offer to be updated
     */
    public void updateOfferQuantity(int newQuantity, int offerID) {
        try {
            updateOfferQuantity.setInt(1, newQuantity);
            updateOfferQuantity.setInt(2, offerID);
            updateOfferQuantity.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve the ID of the most recently placed offer
     * @return ID of the offer
     */
    public int getPlacedOfferID() {
        int offerID = 0;
        ResultSet rs = null;
        try {
            rs = getPlacedOfferID.executeQuery();
            rs.next();
            offerID = rs.getInt(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return offerID;
    }


    /**
     * Retrieve buy offers from the database and return them as a TreeMap
     */
    public TreeMap<Integer, BuyOffer> getBuyOffers() {
        TreeMap<Integer, BuyOffer> buyOffers = new TreeMap<>();
        ResultSet rs = null;
        try {
            getOffers.setString(1, BUY_OFFER);
            rs = getOffers.executeQuery();
            UnitDataSource unitDB = new UnitDataSource();
            while(rs.next()) {
                int orderID = rs.getInt(1);
                int unitID = rs.getInt(3);
                int userID = rs.getInt(4);
                int assetID = rs.getInt(5);
                double price = rs.getDouble(6);
                int quantity = rs.getInt(7);
                String username = unitDB.executeGetUsername(userID);
                String unitName = unitDB.executeGetUnitName(unitID);
                String assetName = unitDB.executeGetAssetName(assetID);
                BuyOffer offer = new BuyOffer(orderID, assetName, quantity, price, username, unitName);
                buyOffers.put(orderID, offer);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return buyOffers;
    }


    /**
     * Retrieve sell offers from the database and return them as a TreeMap
     */
    public TreeMap<Integer, SellOffer> getSellOffers() {
        TreeMap<Integer, SellOffer> sellOffers = new TreeMap<>();
        ResultSet rs = null;
        try {
            getOffers.setString(1, SELL_OFFER);
            rs = getOffers.executeQuery();
            UnitDataSource unitDB = new UnitDataSource();
            while(rs.next()) {
                int orderID = rs.getInt(1);
                int unitID = rs.getInt(3);
                int userID = rs.getInt(4);
                int assetID = rs.getInt(5);
                double price = rs.getDouble(6);
                int quantity = rs.getInt(7);
                String username = unitDB.executeGetUsername(userID);
                String unitName = unitDB.executeGetUnitName(unitID);
                String assetName = unitDB.executeGetAssetName(assetID);
                SellOffer offer = new SellOffer(orderID, assetName, quantity, price, username, unitName);
                sellOffers.put(orderID, offer);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sellOffers;
    }

    /**
     * Closes the connection to the db
     */
    public void close() throws SQLException {
        connection.close();
    }
}
