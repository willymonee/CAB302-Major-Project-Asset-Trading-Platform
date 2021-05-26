package ElectronicAssetTradingPlatform.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
                                                            + "Price_per_unit, Quantity, Date_fulfilled";
    // private static final String DELETE_COMPLETED_TRADE = "";


    private PreparedStatement getTradesByDate;
    private PreparedStatement getTradesAfterDate;
    private PreparedStatement getTradesBetweenDates;
    private PreparedStatement insertCompletedTrade;

    private Connection connection;

    public MarketplaceHistoryDataSource() {
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
}
