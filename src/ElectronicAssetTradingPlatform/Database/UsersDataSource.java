package ElectronicAssetTradingPlatform.Database;

import ElectronicAssetTradingPlatform.Users.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Class for retrieving data from the XML file holding the address list.
 */
public class UsersDataSource {
    private static final String INSERT_USER = "INSERT INTO User_Accounts (User_ID, Username, Password_hash, Salt, User_Type, Unit_ID) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String GET_USER = "SELECT Password_hash, User_Type, Unit_ID FROM User_Accounts WHERE Username = ?";
    private static final String GET_SALT = "SELECT Salt FROM User_Accounts WHERE Username = ?";

    private static final String GET_UNIT_NAME = "SELECT Name FROM Organisational_Units WHERE Unit_ID = ?";
    private static final String GET_UNIT_ID = "SELECT Unit_ID FROM Organisational_Units WHERE Name = ?";

    PreparedStatement getUserQuery;
    PreparedStatement addUserQuery;
    PreparedStatement getSaltQuery;
    PreparedStatement getUnitNameQuery;
    PreparedStatement getUnitIDQuery;

    private Connection connection;

    private PreparedStatement getNameList;

    public UsersDataSource() throws SQLException {
        connection = DBConnectivity.getInstance();

        getUserQuery = connection.prepareStatement(GET_USER);
        addUserQuery = connection.prepareStatement(INSERT_USER);
        getSaltQuery = connection.prepareStatement(GET_SALT);
        getUnitNameQuery = connection.prepareStatement(GET_UNIT_NAME);
        getUnitIDQuery = connection.prepareStatement(GET_UNIT_ID);
    }

    public User getUser(String username) throws SQLException {
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

    public void insertUser(User user, String salt) throws SQLException {
        // Initialise
        // User_ID, Username, Password_hash, Salt, User_Type, Unit_ID
        addUserQuery.setString(1, null);
        addUserQuery.setString(2, user.getUsername());
        addUserQuery.setString(3, user.getPassword());
        addUserQuery.setString(4, salt);
        addUserQuery.setString(5, user.getUserType());

        // Get unit ID
        if (user.getClass() == OrganisationalUnitMembers.class) {
            String id = executeGetUnitID(((OrganisationalUnitMembers) user).getUnitName());

            addUserQuery.setString(6, id);
        }
        else {
            addUserQuery.setString(6, null);
        }

        addUserQuery.execute();
    }

    public String getSalt(User user) throws SQLException {
        // Prepare
        getSaltQuery.setString(1, user.getUsername());

        // Result
        ResultSet rs = null;
        rs = getSaltQuery.executeQuery();

        // Return
        return rs.getString("Salt");
    }

    public String getSalt(String username) throws SQLException {
        // Prepare
        getSaltQuery.setString(1, username);

        // Result
        ResultSet rs = null;
        rs = getSaltQuery.executeQuery();

        // Return
        return rs.getString("Salt");
    }

    private String executeGetUnitName(String unitID) throws SQLException {
        // Prepare
        getUnitNameQuery.setString(1, unitID);

        // Result
        ResultSet rs = null;
        rs = getUnitNameQuery.executeQuery();

        // Return
        return rs.getString("Name");
    }

    private String executeGetUnitID(String unitName) throws SQLException {
        // Prepare
        getUnitIDQuery.setString(1, unitName);

        // Result
        ResultSet rs = null;
        rs = getUnitIDQuery.executeQuery();

        // Return
        return rs.getString("Unit_ID");
    }

    // Close connection
    public void close() throws SQLException {
        connection.close();
    }
}
