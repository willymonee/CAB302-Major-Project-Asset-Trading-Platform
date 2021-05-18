package ElectronicAssetTradingPlatform.Users;

import ElectronicAssetTradingPlatform.Passwords.Hashing;

/**
 * Class for the users of the application
 */
public class User {
    private String username;
    private String password;
    private String salt;
    protected String userType; // So that children can set their own userType in constructor (safety?)

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
     * @param password new string password for the user
     */
    public void changePassword(String password) {
        // Create salt + hashed password
        byte[] saltBytes = Hashing.newRngBytes(Hashing.SALT_SIZE);
        byte[] passwordBytes = Hashing.createHash(saltBytes, password);

        // Store password as string
        this.password = Hashing.bytesToString(passwordBytes);
        // Store salt as string
        this.salt = Hashing.bytesToString(saltBytes);
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


    /**
     * Exception for when an inputed user type is invalid
     */
    public static class UserTypeException extends Exception {
        public UserTypeException(String message) {
            super(message);
        }
    }

    /**
     * Exception for when any mandatory input is empty
     */
    public static class EmptyFieldException extends Exception {
        public EmptyFieldException(String message) {
            super(message);
        }
    }
}
