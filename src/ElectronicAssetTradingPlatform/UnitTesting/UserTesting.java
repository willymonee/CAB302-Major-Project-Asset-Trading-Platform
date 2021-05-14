package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.Database.ETPDataSource;
import ElectronicAssetTradingPlatform.Database.UsersDataSource;
import ElectronicAssetTradingPlatform.Passwords.Hashing;
import ElectronicAssetTradingPlatform.Users.ITAdmin;
import ElectronicAssetTradingPlatform.Users.User;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.AfterClass;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

public class UserTesting {
    // Exception codes: https://sqlite.org/rescode.html
    private static final int CONSTRAINT_EXCEPTION_CODE = 19;
    /*
    INSERT INTO User_Accounts (Username, Password_hash, Salt, User_Type, Unit_ID) VALUES ('adminGuy', 'pass123', 'salt', 'ITAdmin', null);
     */

    ITAdmin itAdmin;
    UsersDataSource db;

    @BeforeEach
    @Test
    public void setUpUser() {
        // Recreate db
        ETPDataSource etp = new ETPDataSource();
        try {
            db = new UsersDataSource();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // create an organisational unit member
        itAdmin = new ITAdmin("adminGuy", "pass123", "salt");

    }

    @Test
    public void editPwd() {
        itAdmin.changePassword("newPassword");

        assertDoesNotThrow(() -> new UsersDataSource().editUserPassword(itAdmin.getUsername(), itAdmin.getPassword(), itAdmin.getSalt()));

        ITAdmin adminGuy = null;
        try {
            adminGuy = (ITAdmin) new UsersDataSource().getUser("adminGuy");
        } catch (SQLException | User.UserTypeException e) {
            e.printStackTrace();
            assert false;
        }

        // Test update
        assertNotEquals("pass123", adminGuy.getPassword());
        assertNotEquals("salt", adminGuy.getSalt());
        // Test password checking works
        assertTrue(Hashing.compareHashPass(itAdmin.getSalt(), "newPassword", itAdmin.getPassword()));
    }

    @AfterClass
    public void close() throws SQLException {
        db.close();
    }
}
