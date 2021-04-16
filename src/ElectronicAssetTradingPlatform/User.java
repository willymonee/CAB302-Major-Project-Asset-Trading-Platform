package ElectronicAssetTradingPlatform;

/**
 * Class for the users of the application
 */
public class User {
    private String username;
    private String password;

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
     * Changes the user's password if certain conditions are met.
     *
     * @param password new string for user to change password to
     * @throws Exception throws exception when...
     */
    public void changePassword(String password) throws Exception {
        // changePassword method
        throw new Exception();
    }

    /**
     * Creates elements of the GUI common in all users’ GUIs, such as the navigation bar and footer
     *
     */
    public void createGUI() {
        // createGUI method
    }

}
