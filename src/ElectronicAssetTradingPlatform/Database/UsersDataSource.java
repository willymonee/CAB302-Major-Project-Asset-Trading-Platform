package dataExercise;

import ElectronicAssetTradingPlatform.Database.DBConnectivity;
import ElectronicAssetTradingPlatform.Users.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.TreeSet;


/**
 * Class for retrieving data from the XML file holding the address list.
 */
public class UsersDataSource {
    private static final String INSERT_USER = "INSERT INTO User_Accounts (User_ID, Username, Password_hash, User_Type, Unit_ID) VALUES (?, ?, ?, ?, ?);";
    private static final String GET_USERS = "SELECT Password_hash, User_Type, Unit_ID FROM User_Accounts WHERE Username = ?";
    private static final String GET_UNIT_NAME = "SELECT Name FROM Organisational_Units WHERE Unit_ID = ?";

    PreparedStatement getUserQuery;
    PreparedStatement getUnitNameQuery;
    PreparedStatement addUserQuery;

    private Connection connection;

    private PreparedStatement getNameList;

    public UsersDataSource() throws SQLException {
        connection = DBConnectivity.getInstance();

        getUserQuery = connection.prepareStatement(GET_USERS);
        getUnitNameQuery = connection.prepareStatement(GET_UNIT_NAME);
        addUserQuery = connection.prepareStatement(INSERT_USER);
    }

    public User getUsers(String username) throws SQLException {
        // Initialise
        getUserQuery.setString(1, username);

        // Query
        ResultSet rs = null;
        rs = getUserQuery.executeQuery();
        // Result
        String passwordHash = rs.getString("Password_hash");
        String userType = rs.getString("User_Type");
        String unitID = rs.getString("Unit_ID");

        // Get user based on user type
        User queriedUser = null;
        switch (User.UserTypeEnum.valueOf(userType)) {
            case ITAdmin -> queriedUser = new ITAdmin(username, passwordHash);
            case OrganisationalUnitMembers -> {
                String unitName = executeGetUnitName(unitID); // Get unit name
                queriedUser = new OrganisationalUnitMembers(username, passwordHash, unitName);
            }
            case OrganisationalUnitLeader -> {
                String unitName = executeGetUnitName(unitID); // Get unit name
                queriedUser = new OrganisationalUnitLeader(username, passwordHash, unitName);
            }
            case SystemsAdmin -> queriedUser = new SystemsAdmin(username, passwordHash);
            default -> throw new SQLException("Invalid user type"); // Temporary - add custom exception later
        }

        return queriedUser;
    }

    public void insertUser(User user) throws SQLException {
        // Initialise
        //INSERT INTO User_Accounts (User_ID, Username, Password_hash, User_Type, Unit_ID) VALUES (?, ?, ?, ?, ?)
        addUserQuery.setString(1, null);
        addUserQuery.setString(2, user.());
    }

    private String executeGetUnitName(String unitID) throws SQLException {
        getUnitNameQuery.setString(1, unitID);

        ResultSet rs = null;
        rs = getUnitNameQuery.executeQuery();

        return rs.getString("Name");
    }

    // Close connection
    public void close() throws SQLException {
        connection.close();
    }
}
