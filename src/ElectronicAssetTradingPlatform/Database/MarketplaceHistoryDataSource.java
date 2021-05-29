package ElectronicAssetTradingPlatform.Database;

import ElectronicAssetTradingPlatform.AssetTrading.BuyOffer;
import ElectronicAssetTradingPlatform.AssetTrading.SellOffer;
import ElectronicAssetTradingPlatform.Database.UnitDataSource;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MarketplaceHistoryDataSource {
    //SQLITE Default Date format: yyyy-MM-dd HH:mm:ss
    // example: '2007-01-01 10:00:00'
    //https://www.sqlitetutorial.net/sqlite-date/
    //Acceptable format: YYY-MM-DD
    //https://www.sqlite.org/lang_datefunc.html
    private static final String GET_TRADES_BY_DATE = "SELECT * FROM Marketplace_history WHERE Date_fulfilled < date(?)";
    private static final String GET_TRADES_AFTER_DATE = "SELECT * FROM Marketplace_history WHERE Date_fulfilled > ?";
    private static final String GET_TRADES_BETWEEN_DATES = "SELECT * FROM Marketplace_history WHERE Date_fulfilled BETWEEN date(?) AND date(?);";
    private static final String INSERT_COMPLETED_TRADE = "INSERT INTRO Marketplace_history (Buyer_ID, Seller_ID, Asset_type_ID,"
                                                            + "Price_per_unit, Quantity, Date_fulfilled)"
                                                            + "VALUES (?, ?, ?, ?, ?, ?);";
    // private static final String DELETE_COMPLETED_TRADE = "";


    private PreparedStatement getTradesByDate;
    private PreparedStatement getTradesAfterDate;
    private PreparedStatement getTradesBetweenDates;
    private PreparedStatement insertCompletedTrade;

    private Connection connection;

    private static class HistorySingletonHolder {
        private final static MarketplaceHistoryDataSource INSTANCE = new MarketplaceHistoryDataSource();
    }

    public static MarketplaceHistoryDataSource getInstance() { return HistorySingletonHolder.INSTANCE; }

    private MarketplaceHistoryDataSource() {
        connection = DBConnectivity.getInstance();
        try {
            getTradesByDate = connection.prepareStatement(GET_TRADES_BY_DATE);
            getTradesAfterDate = connection.prepareStatement(GET_TRADES_AFTER_DATE);
            getTradesBetweenDates = connection.prepareStatement(GET_TRADES_BETWEEN_DATES);
            insertCompletedTrade = connection.prepareStatement(INSERT_COMPLETED_TRADE);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Some data structure to store multiple values
    public void getTradesByDate(Date date) throws SQLException {
        ResultSet rs = null;
        getTradesByDate.setString(1, date.toString());
    }
    /*  When displaying the graph, im guessing x axis is date and y axis is price per unit
        Will need to display or create graph for each unique asset in the database
        and the quantity would come into play um.
        Worst case scenario just display a table of data lmaooo, i guess just rip some columns straight out db
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

            if (buyOffer.getPricePerUnit() == sellOffer.getPricePerUnit()) {
                double ppu = buyOffer.getPricePerUnit();
                String price = Double.toString(ppu);
                insertCompletedTrade.setString(4, price);
            }

            else
                execute = false;



               insertCompletedTrade.setInt(5, quantity);


            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime currentTime = LocalDateTime.now();
            String dateCompleted = dateFormatter.format(currentTime).toString();
            insertCompletedTrade.setString(6, dateCompleted);

            if (execute) {
                insertCompletedTrade.execute();
            }

            else
                throw new SQLException();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }





}


