package ElectronicAssetTradingPlatform.Database;

import ElectronicAssetTradingPlatform.Exceptions.UserTypeException;
import ElectronicAssetTradingPlatform.Users.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


/**
 * Class for retrieving data from the database
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

    /**
     * Singleton of data source
     */
    private static class SingletonHolder {
        private final static UsersDataSource INSTANCE = new UsersDataSource();
    }
    public static UsersDataSource getInstance() { return SingletonHolder.INSTANCE; }

    /**
     * Connect to DB and initialise queries
     */
    private UsersDataSource() {
        connection = DBConnectivity.getInstance();

        try {
            addUserQuery = connection.prepareStatement(INSERT_USER);
            getUserQuery = connection.prepareStatement(GET_USER, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            editUserQuery = connection.prepareStatement(EDIT_USER);
            editPasswordQuery = connection.prepareStatement(EDIT_PASSWORD);
            getUnitCreditsQuery = connection.prepareStatement(GET_UNIT_CREDITS);
            getUnitAssetsQuery = connection.prepareStatement(GET_UNIT_ASSETS);
        } catch (SQLException e) {
            System.out.println("UsersDataSource constructor error: ");
            e.printStackTrace();
        }
    }

    /**
     * Gets a user from the database
     *
     * @param username string username of user to get
     * @return the queried user from the database
     * @throws SQLException Throws database query errors
     * @throws UserTypeException Throws exception when the user type in the database is wrong
     */
    public User getUser(String username) throws SQLException, UserTypeException {
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
            if (rs.isClosed()) throw new SQLException("User not found: " + username);
            rs.next();


            // Result
            passwordHash = rs.getString("Password_hash");
            salt = rs.getString("Salt");
            userType = rs.getString("User_Type");
            unitName = rs.getString("Unit_Name");
        } finally {
            if (rs != null) rs.close();
        }

        // Try get type
        UsersFactory.UserType type;
        try {
            type = UsersFactory.UserType.valueOf(userType);
        }
        catch (IllegalArgumentException e) {
            throw new UserTypeException("Invalid user type in database");
        }

        return UsersFactory.CreateUser(username, passwordHash, salt, unitName, type);
    }

    /**
     * Inserts a user to the database
     *
     * @param user User to be inserted to the database
     * @throws SQLException Throws database query errors
     */
    public void insertUser(User user) throws SQLException {
        // Initialise
        addUserQuery.setString(1, user.getUsername());
        addUserQuery.setString(2, user.getPassword());
        addUserQuery.setString(3, user.getSalt());
        addUserQuery.setString(4, user.getUserType());

        // Get unit ID
        if (user.getClass() == OrganisationalUnitMembers.class || user.getClass() == OrganisationalUnitLeader.class) {
            UnitDataSource unitDB = new UnitDataSource();
            String id = unitDB.executeGetUnitID(((OrganisationalUnitMembers) user).getUnitName());

            addUserQuery.setString(5, id);
        }
        else {
            addUserQuery.setString(5, null);
        }

        addUserQuery.execute();
    }

    /**
     * Edits an existing user in the database
     *
     * @param username Username of the user to be edited
     * @param userType Edited type of the user
     * @param unitName Edited unit of the user
     * @throws SQLException Throws database query errors
     */
    public void editUser(String username, String userType, String unitName) throws SQLException, UserTypeException {
        // Check user is in database
        getUser(username);

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

    /**
     * Edits an existing user's password in the database
     *
     * @param username Username of the user to be edited
     * @param password New password of the user
     * @param salt New salt for the password
     * @throws SQLException Throws database query errors
     */
    public void editUserPassword(String username, String password, String salt) throws SQLException {
        // Initialise
        editPasswordQuery.setString(1, password);
        editPasswordQuery.setString(2, salt);
        editPasswordQuery.setString(3, username);

        editPasswordQuery.execute();
    }

    /**
     * Gets the unit's credits
     *
     * @param unitName Name of the unit to get from
     * @return Credits of the unit
     * @throws SQLException Throws database query errors
     */
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

    /**
     * Gets the unit's assets and its quantities
     *
     * @param unitName Name of the unit to get from
     * @return Assets, and its quantities, of the unit
     * @throws SQLException Throws database query errors
     */
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

    /**
     * Closes the database connection
     *
     * @throws SQLException Throws database query errors
     */
    public void close() throws SQLException {
        connection.close();
    }
}
