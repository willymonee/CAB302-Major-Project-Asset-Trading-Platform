package ElectronicAssetTradingPlatform.Database;

import ElectronicAssetTradingPlatform.Users.*;
import ElectronicAssetTradingPlatform.Database.UnitDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Class for retrieving data from the XML file holding the address list.
 */
public class UsersDataSource extends UnitDataSource{
    private static final String INSERT_USER = "INSERT INTO User_Accounts (User_ID, Username, Password_hash, Salt, User_Type, Unit_ID) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String GET_USER = "SELECT Password_hash, User_Type, Salt, Unit_ID FROM User_Accounts WHERE Username = ?";
    private static final String GET_SALT = "SELECT Salt FROM User_Accounts WHERE Username = ?";

    private static final String GET_UNIT_NAME = "SELECT Name FROM Organisational_Units WHERE Unit_ID = ?";
    private static final String GET_UNIT_ID = "SELECT Unit_ID FROM Organisational_Units WHERE Name = ?";

    PreparedStatement getUserQuery;
    PreparedStatement addUserQuery;
    PreparedStatement getSaltQuery;
    //PreparedStatement getUnitNameQuery;
    //PreparedStatement getUnitIDQuery;

    private Connection connection;

    public UsersDataSource() throws SQLException {
        connection = DBConnectivity.getInstance();

        addUserQuery = connection.prepareStatement(INSERT_USER);
        getUserQuery = connection.prepareStatement(GET_USER);
        getSaltQuery = connection.prepareStatement(GET_SALT);
        //getUnitNameQuery = connection.prepareStatement(GET_UNIT_NAME);
        //getUnitIDQuery = connection.prepareStatement(GET_UNIT_ID);
    }

    public User getUser(String username) throws SQLException, User.UserTypeException {
        // Initialise
        getUserQuery.setString(1, username);

        // Query
        ResultSet rs = null;
        rs = getUserQuery.executeQuery();
        // Result
        String passwordHash = rs.getString("Password_hash");
        String salt = rs.getString("Salt");
        String userType = rs.getString("User_Type");
        String unitID = rs.getString("Unit_ID");

        // Get user based on user type
        User queriedUser = null;
        switch (User.UserTypeEnum.valueOf(userType)) {
            case ITAdmin -> queriedUser = new ITAdmin(username, passwordHash, salt);
            case OrganisationalUnitMembers -> {
                String unitName = executeGetUnitName(unitID); // Get unit name
                queriedUser = new OrganisationalUnitMembers(username, passwordHash, salt, unitName);
            }
            case OrganisationalUnitLeader -> {
                String unitName = executeGetUnitName(unitID); // Get unit name
                queriedUser = new OrganisationalUnitLeader(username, passwordHash, salt, unitName);
            }
            case SystemsAdmin -> queriedUser = new SystemsAdmin(username, passwordHash, salt);
            default -> throw new User.UserTypeException("Invalid user type");
        }

        return queriedUser;
    }

    public void insertUser(User user) throws SQLException {
        // Initialise
        // User_ID, Username, Password_hash, Salt, User_Type, Unit_ID
        addUserQuery.setString(1, null);
        addUserQuery.setString(2, user.getUsername());
        addUserQuery.setString(3, user.getPassword());
        addUserQuery.setString(4, user.getSalt());
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


    // Close connection
    public void close() throws SQLException {
        connection.close();
    }
}
