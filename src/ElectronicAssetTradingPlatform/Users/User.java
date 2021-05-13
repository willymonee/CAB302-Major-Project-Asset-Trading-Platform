package ElectronicAssetTradingPlatform.Users;

/**
 * Class for the users of the application
 */
public class User {
    private String username;
    private String password;
    private String salt;
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
    public User(String username, String password, String salt) {
        // Users constructor
        this.username = username;
        this.password = password;
        this.salt = salt;
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
    public String getSalt() {return salt;}
    public String getUserType() {return userType;}


    public static class UserTypeException extends Exception {
        public UserTypeException(String message) {
            super(message);
        }
    }

    public static class EmptyFieldException extends Exception {
        public EmptyFieldException(String message) {
            super(message);
        }
    }
}
