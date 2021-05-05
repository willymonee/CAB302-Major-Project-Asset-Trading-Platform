package ElectronicAssetTradingPlatform.Database;
import java.sql.*;
import java.util.Set;
import java.util.TreeSet;
import java.sql.SQLException;

public class ETPDataSource {
    // DB Instance
    private Connection connection;
    // Create Table Queries
    private static final String createMarketplaceTable =
            "CREATE TABLE IF NOT EXISTS MARKETPLACE (" +
                    "Offer_ID INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */ NOT NULL UNIQUE," +
                    /*"Buy_or_Sell ENUM('buy', 'sell') NOT NULL," +*/
                    "Unit_ID INTEGER NOT NULL," +
                    "User_ID INTEGER NOT NULL," +
                    "Asset_type_ID NOT NULL," +
                    "Price_per_unit FLOAT NOT NULL" +
                    ");";


    public ETPDataSource() {
        connection = DBConnectivity.getInstance();
        try {
            Statement st = connection.createStatement();
            st.execute(createMarketplaceTable);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
