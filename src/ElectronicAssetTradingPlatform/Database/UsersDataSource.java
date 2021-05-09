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
    private static final String GET_USERS = "SELECT Password_hash, User_Type, Unit_ID FROM User_Accounts WHERE Username = ?";

    private static final String GET_UNIT_NAME = "SELECT Name FROM Organisational_Units WHERE Unit_ID = ?";
    private static final String GET_UNIT_ID = "SELECT Unit_ID FROM Organisational_Units WHERE Name = ?";

    PreparedStatement getUserQuery;
    PreparedStatement addUserQuery;
    PreparedStatement getUnitNameQuery;
    PreparedStatement getUnitIDQuery;

    private Connection connection;

    private PreparedStatement getNameList;

    public UsersDataSource() throws SQLException {
        connection = DBConnectivity.getInstance();

        getUserQuery = connection.prepareStatement(GET_USERS);
        addUserQuery = connection.prepareStatement(INSERT_USER);
        getUnitNameQuery = connection.prepareStatement(GET_UNIT_NAME);
        getUnitIDQuery = connection.prepareStatement(GET_UNIT_ID);
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

    private String executeGetUnitName(String unitID) throws SQLException {
        getUnitNameQuery.setString(1, unitID);

        ResultSet rs = null;
        rs = getUnitNameQuery.executeQuery();

        return rs.getString("Name");
    }

    private String executeGetUnitID(String unitName) throws SQLException {
        getUnitIDQuery.setString(1, unitName);

        ResultSet rs = null;
        rs = getUnitIDQuery.executeQuery();

        return rs.getString("Unit_ID");
    }

    // Close connection
    public void close() throws SQLException {
        connection.close();
    }
}
