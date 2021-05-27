package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.Database.ETPDataSource;
import ElectronicAssetTradingPlatform.Database.UsersDataSource;
import ElectronicAssetTradingPlatform.Passwords.Hashing;
import ElectronicAssetTradingPlatform.Users.ITAdmin;

import static org.junit.jupiter.api.Assertions.*;

import ElectronicAssetTradingPlatform.Exceptions.UserTypeException;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

public class UserTesting {
    // Exception codes: https://sqlite.org/rescode.html
    private static final int CONSTRAINT_EXCEPTION_CODE = 19;
    /*
    INSERT INTO User_Accounts (Username, Password_hash, Salt, User_Type, Unit_ID) VALUES ('adminGuy', 'pass123', 'salt', 'ITAdmin', null);
     */

    ITAdmin itAdmin;
    static UsersDataSource db;

    @BeforeEach
    @Test
    public void setUpUser() {
        // Recreate db
        ETPDataSource etp = new ETPDataSource();
        db = UsersDataSource.getInstance();
        // create an organisational unit member
        itAdmin = new ITAdmin("adminGuy", "pass123", "salt");

    }

    @Test
    public void editPwd() {
        itAdmin.changePassword("newPassword");

        assertDoesNotThrow(() -> UsersDataSource.getInstance().editUserPassword(itAdmin.getUsername(), itAdmin.getPassword(), itAdmin.getSalt()));

        ITAdmin adminGuy = null;
        try {
            adminGuy = (ITAdmin) UsersDataSource.getInstance().getUser("adminGuy");
        } catch (SQLException | UserTypeException e) {
            e.printStackTrace();
            assert false;
        }

        // Test update
        assertNotEquals("pass123", adminGuy.getPassword());
        assertNotEquals("salt", adminGuy.getSalt());
        // Test password checking works
        assertTrue(Hashing.compareHashPass(itAdmin.getSalt(), "newPassword", itAdmin.getPassword()));
    }

    @AfterAll
    public static void close() throws SQLException {
        db.close();
    }
}
