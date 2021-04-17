package ElectronicAssetTradingPlatform;

/**
 * Class for Systems Admin users allowing for all users with this access level to be able to
 * create new Organisational Units, be able to edit their respective Assets and Credits
 * along with having the ability to create new asset types and new uses accounts with up to the
 * same level of access privileges. On top of this they ............ about overlooking network
 * and account security.
 */
public class SystemsAdmin extends User{

    /**
     * Constructor for Systems Admin
     *
     * @param username
     * @param password
     */

    public SystemsAdmin(String username, String password) {
        super(username, password);
    }

    /**
     * Initialise the Database
     */

    public void initDB() {

    }

    /**
     * Backup the Database
     */
    public void backupDB() {

    }
}
