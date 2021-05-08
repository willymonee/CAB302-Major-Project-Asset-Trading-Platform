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

    /**
     * Allows connection to the database to query any tables within the schema.
     * @param query SQL Query string
     * @return true if the statement was executed else false if the statement was not executed.
     */
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

    /**
     * Close Connection to the database
     */
    public void close() {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
}
