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
    private static final String GET_USER =
            "SELECT Password_hash, User_Type, Salt, Organisational_Units.Name as Unit_Name " +
            "FROM User_Accounts " +
            "LEFT OUTER JOIN Organisational_Units " +
            "ON User_Accounts.Unit_ID = Organisational_Units.Unit_ID " +
            "WHERE Username = ?";

    PreparedStatement getUserQuery;
    PreparedStatement addUserQuery;

    private Connection connection;

    public UsersDataSource() throws SQLException {
        connection = DBConnectivity.getInstance();

        addUserQuery = connection.prepareStatement(INSERT_USER);
        getUserQuery = connection.prepareStatement(GET_USER);
    }

    public User getUser(String username) throws SQLException, User.UserTypeException {
        // Initialise
        getUserQuery.setString(1, username);

        // Query
        ResultSet rs = getUserQuery.executeQuery();
        // Result
        String passwordHash = rs.getString("Password_hash");
        String salt = rs.getString("Salt");
        String userType = rs.getString("User_Type");
        String unitName = rs.getString("Unit_Name");

        // Get user based on user type
        User queriedUser;
        switch (User.UserTypeEnum.valueOf(userType)) {
            case ITAdmin -> queriedUser = new ITAdmin(username, passwordHash, salt);
            case OrganisationalUnitMembers -> queriedUser = new OrganisationalUnitMembers(username, passwordHash, salt, unitName);
            case OrganisationalUnitLeader -> queriedUser = new OrganisationalUnitLeader(username, passwordHash, salt, unitName);
            case SystemsAdmin -> queriedUser = new SystemsAdmin(username, passwordHash, salt);
            default -> throw new User.UserTypeException("Invalid user type");
        }

        return queriedUser;
    }

    public void insertUser(User user) throws SQLException {
        // Initialise
        addUserQuery.setString(1, null);
        addUserQuery.setString(2, user.getUsername());
        addUserQuery.setString(3, user.getPassword());
        addUserQuery.setString(4, user.getSalt());
        addUserQuery.setString(5, user.getUserType());

        // Get unit ID
        if (user.getClass() == OrganisationalUnitMembers.class) {
            UnitDataSource unitDB = new UnitDataSource();
            String id = unitDB.executeGetUnitID(((OrganisationalUnitMembers) user).getUnitName());

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
