package ElectronicAssetTradingPlatform.Database;

// Import dependencies
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectivity {

    // Control the instance of a connection
    private static Connection instance = null;

    /**
     * Constructor to initialise a connection to the database
     */
    private DBConnectivity() {
        Properties dbProperties = new Properties();
        FileInputStream input = null;

        try {
            input = new FileInputStream("./db.props");
            dbProperties.load(input);
            input.close();

            // Identify database and authentication to connect
            String url = dbProperties.getProperty("jdbc.url");
            String username = dbProperties.getProperty("jdbc.username");
            String pw = dbProperties.getProperty("jdbc.pw");
            String schema = dbProperties.getProperty("jdbc.schema");

            // Create the connection
            instance = DriverManager.getConnection(url + "/" + schema, username, pw);
        }

        // maybe make my own sql exception?
        catch (SQLException sqle) {
            System.err.println(sqle);
        }

        // maybe make my own file exception
        catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        }

        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            new DBConnectivity();
        }
        return instance;
    }
}
