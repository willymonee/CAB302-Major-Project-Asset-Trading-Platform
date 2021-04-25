package ElectronicAssetTradingPlatform.Users;

/**
 * Class for Systems Admin users allowing for all users with this access level to be able to
 * create new Organisational Units, be able to edit their respective Assets and Credits
 * along with having the ability to create new asset types and new uses accounts with up to the
 * same level of access privileges. On top of this they ............ about overlooking network
 * and account security.
 */
public class SystemsAdmin extends User {

    /**
     * Constructor for Systems Admin
     *
     * @param username string identifier used to login
     * @param password string matched with username identifier used to login
     */
    public SystemsAdmin(String username, String password) {
        super(username, password);
    }

    /**
     * Initialise the Database
     */
    public void initDB() {
        // Might not need - db should be initialised in the server already, not when the admin wants to
    }

    /**
     * Backup the Database
     */
    public void backupDB() {

    }
}
