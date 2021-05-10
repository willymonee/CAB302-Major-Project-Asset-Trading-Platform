package ElectronicAssetTradingPlatform.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MarketplaceDataSource {
    private static final String INSERT_OFFER = "INSERT INTO Marketplace (Trade_ID, Buy_or_Sell, "
            + "Unit_ID, User_ID, Asset_type_ID, Price_per_Unit)"
            + "VALUES (?, ?, ?, ?, ?, ?);";
    private static final String GET_BUYOFFER = "SELECT * FROM Marketplace WHERE Buy_or_Sell=b?";
    private static final String GET_SELLOFFER = "SELECT * FROM Marketplace WHERE Buy_or_Sell=s?";
    private static final String RESOLVE_OFFER = "DELETE FROM Marketplace WHERE Trade_ID=?";

    private PreparedStatement addOffer;
    private PreparedStatement getBuyOffer;
    private PreparedStatement getSellOffer;
    private PreparedStatement resolveOffer;
}
