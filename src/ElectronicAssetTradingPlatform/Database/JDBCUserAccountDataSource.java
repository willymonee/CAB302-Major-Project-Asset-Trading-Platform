package ElectronicAssetTradingPlatform.Database;

// import dependencies
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.TreeSet;

public class JDBCUserAccountDataSource {
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS `User_Accounts` ("
                + "User_ID INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */ NOT NULL UNIQUE,"
                + "Username VARCHAR(60) NOT NULL UNIQUE,"
                + "Password_hash VARCHAR(80) NOT NULL,"
                + "User_Type VARCHAR(60) NOT NULL,"
                + "Unit_ID INTEGER" + ");";

    private Connection connection;

    public JDBCUserAccountDataSource() {
        connection = DBConnectivity.getInstance();
        try {
            Statement st = connection.createStatement();
            st.execute(CREATE_TABLE);
        }

        catch (SQLException e){
            e.printStackTrace();
        }
    }
}


