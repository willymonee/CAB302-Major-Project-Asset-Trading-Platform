package ElectronicAssetTradingPlatform.Users;

/**
 * Class for the users of the application
 */
public class User {
    private String username;
    private String password;
    protected String userType; // So that children can set their own userType in constructor (safety?)

    // Available user type enum
    public enum UserTypeEnum {
        ITAdmin, OrganisationalUnitLeader, OrganisationalUnitMembers, SystemsAdmin
    }

    /**
     * Constructor used for constructing a default user.
     *
     * @param username string identifier used to login
     * @param password string matched with username identifier used to login
     *
     */
    public User(String username, String password) {
        // Users constructor
        this.username = username;
        this.password = password;
    }

    /**
     * Changes the user's password if certain conditions are met. [C]
     *
     * @param password new string for user to change password to
     * @throws Exception throws exception when...
     */
    public void changePassword(String password) throws Exception {
        // changePassword method
        throw new Exception();
    }

    /**
     * Connect organisational unit member to the server using the supplied
     * configuration file
     *
     */
    public void connectDB() {
        // connectDB method
    }

    public String getUsername() {return username;}
    public String getPassword() {return password;}
}
