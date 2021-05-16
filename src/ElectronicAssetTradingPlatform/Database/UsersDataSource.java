package ElectronicAssetTradingPlatform.Database;

import ElectronicAssetTradingPlatform.Users.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


/**
 * Class for retrieving data from the XML file holding the address list.
 */
public class UsersDataSource {
    private static final String INSERT_USER = "INSERT INTO User_Accounts (Username, Password_hash, Salt, User_Type, Unit_ID) VALUES (?, ?, ?, ?, ?);";
    private static final String GET_USER =
            "SELECT Password_hash, User_Type, Salt, Organisational_Units.Name as Unit_Name " +
            "FROM User_Accounts " +
            "LEFT OUTER JOIN Organisational_Units " +
            "ON User_Accounts.Unit_ID = Organisational_Units.Unit_ID " +
            "WHERE Username = ?";
    private static final String EDIT_USER = "UPDATE User_Accounts SET User_Type = ?, Unit_ID = ? WHERE Username = ?";
    private static final String EDIT_PASSWORD = "UPDATE User_Accounts SET Password_hash = ?, Salt = ? WHERE Username = ?";
    private static final String GET_UNIT_CREDITS =
            "SELECT Credits " +
            "FROM  Organisational_Units " +
            "WHERE name = ?";
    private static final String GET_UNIT_ASSETS =
            "SELECT Asset_Types.Name, Asset_Quantity " +
            "FROM Organisational_Units " +
            "LEFT OUTER JOIN Organisational_Unit_Assets " +
                "ON Organisational_Units.Unit_ID = Organisational_Unit_Assets.Unit_ID " +
            "LEFT OUTER JOIN Asset_Types " +
                "ON Organisational_Unit_Assets.Asset_ID = Asset_Types.Type_ID " +
            "WHERE Organisational_Units.Name = ?";

    PreparedStatement getUserQuery;
    PreparedStatement addUserQuery;
    PreparedStatement editUserQuery;
    PreparedStatement editPasswordQuery;
    PreparedStatement getUnitCreditsQuery;
    PreparedStatement getUnitAssetsQuery;

    private Connection connection;

    public UsersDataSource() throws SQLException {
        connection = DBConnectivity.getInstance();

        addUserQuery = connection.prepareStatement(INSERT_USER);
        getUserQuery = connection.prepareStatement(GET_USER, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        editUserQuery = connection.prepareStatement(EDIT_USER);
        editPasswordQuery = connection.prepareStatement(EDIT_PASSWORD);
        getUnitCreditsQuery = connection.prepareStatement(GET_UNIT_CREDITS);
        getUnitAssetsQuery = connection.prepareStatement(GET_UNIT_ASSETS);
    }

    public User getUser(String username) throws SQLException, User.UserTypeException {
        // Initialise
        getUserQuery.setString(1, username);

        // Query
        ResultSet rs = null;
        String passwordHash;
        String salt;
        String userType;
        String unitName;
        try {
            rs = getUserQuery.executeQuery();

            // Result
            passwordHash = rs.getString("Password_hash");
            salt = rs.getString("Salt");
            userType = rs.getString("User_Type");
            unitName = rs.getString("Unit_Name");
        } finally {
            if (rs != null) rs.close();
        }

        // Get user based on user type
        User queriedUser;
        try {
            switch (User.UserTypeEnum.valueOf(userType)) {
                case ITAdmin -> queriedUser = new ITAdmin(username, passwordHash, salt);
                case OrganisationalUnitMembers -> queriedUser = new OrganisationalUnitMembers(username, passwordHash, salt, unitName);
                case OrganisationalUnitLeader -> queriedUser = new OrganisationalUnitLeader(username, passwordHash, salt, unitName);
                case SystemsAdmin -> queriedUser = new SystemsAdmin(username, passwordHash, salt);
                default -> throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            throw new User.UserTypeException("Invalid user type");
        }

        return queriedUser;
    }

    public void insertUser(User user) throws SQLException {
        // Initialise
        addUserQuery.setString(1, user.getUsername());
        addUserQuery.setString(2, user.getPassword());
        addUserQuery.setString(3, user.getSalt());
        addUserQuery.setString(4, user.getUserType());

        // Get unit ID
        if (user.getClass() == OrganisationalUnitMembers.class) {
            UnitDataSource unitDB = new UnitDataSource();
            String id = unitDB.executeGetUnitID(((OrganisationalUnitMembers) user).getUnitName());

            addUserQuery.setString(5, id);
        }
        else {
            addUserQuery.setString(5, null);
        }

        addUserQuery.execute();
    }

    public void editUser(String username, String userType, String unitName) throws SQLException {
        // Initialise
        editUserQuery.setString(1, userType);
            // Get unit ID
        String unitID = null;
        if (unitName != null) {
            UnitDataSource unitDB = new UnitDataSource();
            unitID = unitDB.executeGetUnitID(unitName);
        }
        editUserQuery.setString(2, unitID);
        editUserQuery.setString(3, username);

        editUserQuery.execute();
    }

    public void editUserPassword(String username, String password, String salt) throws SQLException {
        // Initialise
        editPasswordQuery.setString(1, password);
        editPasswordQuery.setString(2, salt);
        editPasswordQuery.setString(3, username);

        editPasswordQuery.execute();
    }

    public float getUnitCredits(String unitName) throws SQLException {
        // Initialise
        getUnitCreditsQuery.setString(1, unitName);

        // Query
        ResultSet rs = null;
        String unitCredits;
        try {
            rs = getUnitCreditsQuery.executeQuery();

            // Result
            unitCredits = rs.getString("Credits");
        } finally {
            if (rs != null) rs.close();
        }

        // Convert to float
        return Float.parseFloat(unitCredits);
    }

    public HashMap<String, Integer> getUnitAssets(String unitName) throws SQLException {
        // Initialise
        getUnitAssetsQuery.setString(1, unitName);

        // Query
        ResultSet rs = null;
        HashMap<String, Integer> unitAssets = new HashMap<>();
        try {
            rs = getUnitAssetsQuery.executeQuery();

            // Result
            while (rs.next()) {
                String name = rs.getString("Name");
                Integer quantity = Integer.parseInt(rs.getString("Asset_Quantity"));
                unitAssets.put(name, quantity);
            }
        } finally {
            if (rs != null) rs.close();
        }

        return unitAssets;
    }

    // Close connection
    public void close() throws SQLException {
        connection.close();
    }
}
