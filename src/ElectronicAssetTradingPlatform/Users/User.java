package ElectronicAssetTradingPlatform.Users;

import ElectronicAssetTradingPlatform.Exceptions.EmptyFieldException;
import ElectronicAssetTradingPlatform.Passwords.Hashing;

import java.io.Serializable;

/**
 * Class for the users of the application
 */
public class User implements Serializable {
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
     * Get Username of the User
     * @return  String of the username
     */
    public String getUsername() {return username;}

    /**
     * Get password of the User
     * @return  String matched password for User
     */
    public String getPassword() {return password;}

    /**
     * Gets the salt value for the password
     * @return String salt value
     */
    public String getSalt() {return salt;}

    /**
     * Gets the User Type of the User
     * @return  String of type of User
     */
    public String getUserType() {return userType;}


}
