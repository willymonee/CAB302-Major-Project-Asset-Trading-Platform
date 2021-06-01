package ElectronicAssetTradingPlatform.Database;

import ElectronicAssetTradingPlatform.AssetTrading.BuyOffer;
import ElectronicAssetTradingPlatform.AssetTrading.SellOffer;
import ElectronicAssetTradingPlatform.AssetTrading.TradeHistory;



import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class MarketplaceHistoryDataSource {

    private static final String INSERT_COMPLETED_TRADE = "INSERT INTO Marketplace_history (Buyer_ID, Seller_ID, Asset_type_ID,"
                                                            + "Price_per_unit, Quantity, Date_fulfilled)"
                                                            + "VALUES (?, ?, ?, ?, ?, ?);";
    private static final String GET_ASSET_HISTORY = "SELECT Price_per_unit, Date_fulfilled FROM Marketplace_history WHERE Asset_type_ID = ?;";


    private PreparedStatement insertCompletedTrade;
    private PreparedStatement getAssetHistory;

    private Connection connection;

    /**
     * Singleton Holder for MarketplaceHistoryDataSource
     */
    private static class HistorySingletonHolder {
        private final static MarketplaceHistoryDataSource INSTANCE = new MarketplaceHistoryDataSource();
    }

    /**
     * Get Instance via Singleton
     */
    public static MarketplaceHistoryDataSource getInstance() { return HistorySingletonHolder.INSTANCE; }

    /**
     * Constructor for MarketplaceHistory Data Source
     */
    private MarketplaceHistoryDataSource() {
        connection = DBConnectivity.getInstance();
        try {
            insertCompletedTrade = connection.prepareStatement(INSERT_COMPLETED_TRADE);
            getAssetHistory = connection.prepareStatement(GET_ASSET_HISTORY);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts the relevant data into the Marketplace_history database given a completed trade
     * @param buyOffer      A buy offer
     * @param sellOffer     A sell offer
     * @param quantity      The quantity of assets traded
     */
    public void insertCompletedTrade(BuyOffer buyOffer, SellOffer sellOffer, int quantity) {
        boolean execute = true;
        try {
            UnitDataSource unitDB = new UnitDataSource();
            String buyerID = unitDB.executeGetUserID(buyOffer.getUsername());
            insertCompletedTrade.setString(1, buyerID);
            String sellerID = unitDB.executeGetUserID(sellOffer.getUsername());
            insertCompletedTrade.setString(2, sellerID);
            if (buyOffer.getAssetName() == sellOffer.getAssetName()) {
                int assetID = unitDB.executeGetAssetID(buyOffer.getAssetName());
                insertCompletedTrade.setInt(3, assetID);
            }

            else
                execute = false;


            insertCompletedTrade.setString(4, String.valueOf(sellOffer.getPricePerUnit()));
            insertCompletedTrade.setString(5, String.valueOf(quantity));

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime currentTime = LocalDateTime.now();
            String dateCompleted = dateFormatter.format(currentTime).toString();
            insertCompletedTrade.setString(6, dateCompleted);

            if (execute) {
                insertCompletedTrade.execute();
            }

            else
                throw new SQLException();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Returns all buy & sell trades completed by a organisational unit
     * @param unitID    The ID of the organisational Unit
     * @return          The trade history of the queried organisational unit
     */
    public TreeMap<Integer, TradeHistory> getUnitTradeHistory(int unitID) {
        TreeMap<Integer, TradeHistory> unitTradeHistory = new TreeMap<>();
        ResultSet rs = null;
        // Need SQL query to print out all the trades given a unit ID, where the buyerID/seller ID has unitID = unitID


        return unitTradeHistory;
    }

    /**
     * Gets the price history of an asset
     * @param assetName       String name of the asset's history which is being queried
     * @return              A HashMap of the asset's previously sold price as
     *                      float and date a trade for this asset was completed
     */
    public List<List<Object>> getAssetPriceHistory(String assetName) {
        List<List<Object>> assetPriceHistory = new ArrayList<List<Object>>();
        ResultSet rs = null;
        try {
            // Get ID
            int assetID = UnitDataSource.getInstance().executeGetAssetID(assetName);

            getAssetHistory.setInt(1, assetID);
            rs = getAssetHistory.executeQuery();
            while(rs.next()) {
                float price = rs.getFloat(1);

                String dateTraded = rs.getString(2);
                // Convert string to Date
                Date date = java.sql.Date.valueOf(dateTraded);
                ArrayList<Object> newRow = new ArrayList<>();
                newRow.add(date);
                newRow.add(price);
                assetPriceHistory.add(newRow);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return assetPriceHistory;
    }

}




