package ElectronicAssetTradingPlatform.Users;

import java.io.*;

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
    public SystemsAdmin(String username, String password, String salt) {
        super(username, password, salt);
        this.userType = UsersFactory.UserType.SystemsAdmin.toString();
    }

    private String DB_FILENAME = "ETP.db";
    private String BACKUP_FILENAME = "dbBackup.db";
    /**
     * Copies the DB file and backs it up as a separate file
     */
    public void backupDB() {
        // From https://www.baeldung.com/java-copy-file
        try (
                InputStream in = new BufferedInputStream(
                        new FileInputStream(DB_FILENAME));
                OutputStream out = new BufferedOutputStream(
                        new FileOutputStream(BACKUP_FILENAME))) {

            byte[] buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
