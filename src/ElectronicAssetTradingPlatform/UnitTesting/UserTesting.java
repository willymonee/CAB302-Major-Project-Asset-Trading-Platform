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

    ITAdmin itAdmin;

    @BeforeEach
    @Test
    public void setUpUser() {
        // Recreate db
        itAdmin = new ITAdmin("adminGuy", "pass123", "salt");

    }

    @Test
    public void editPwd() {
        itAdmin.changePassword("newPassword");

        // Test update
        assertNotEquals("pass123", itAdmin.getPassword());
        assertNotEquals("salt", itAdmin.getSalt());
        // Test password checking works
        assertTrue(Hashing.compareHashPass(itAdmin.getSalt(), "newPassword", itAdmin.getPassword()));
    }
}
