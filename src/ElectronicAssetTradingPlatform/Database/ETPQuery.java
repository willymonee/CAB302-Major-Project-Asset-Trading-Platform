package ElectronicAssetTradingPlatform.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/***
 * Class to handle querying of the DB
 */
public class ETPQuery {
    // DB Instance
    private Connection connection;

    // Constructor
    public ETPQuery() {

    }

    public boolean queryDB(String query) {
        connection = DBConnectivity.getInstance();
        try {
            if (query.toUpperCase().contains("SELECT")) {
                Statement st = connection.createStatement();
                st.execute(query);
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
