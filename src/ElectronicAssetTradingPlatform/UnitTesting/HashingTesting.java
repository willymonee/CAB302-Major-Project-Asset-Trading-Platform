package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.Passwords.Hashing;

import static org.junit.Assert.*;
import org.junit.jupiter.api.*;

public class HashingTesting {

    String salt;
    String storedPwd;

    @BeforeEach @Test
    public void setUpInputStr() {
        // Create mock data
        byte[] saltB = {47, 80, -101, -47, -72, 46, -10, 60, 69, -11, 124, 100, 87, 58, 29, 1};
        byte[] storedPwdB = {-42, 33, -71, 49, 118, -115, -77, 17, 24, -41, 122, -70, 22, -6, -63, -80, 30, 112, -44, -87, 95, 96, -25, -62, -65, -36, -125, -27, -74, 51, 117, -119, -12, -63, 32, 27, -81, 69, -62, 42, -43, -13, 106, -66, -62, -67, 22, 42, 51, 38, 9, -125, -17, 93, 52, -19, 16, -88, 5, -27, 28, 66, 107, -92};
        salt = Hashing.bytesToString(saltB);
        storedPwd = Hashing.bytesToString(storedPwdB);

    }

    // Test correct password input
    @Test
    public void correctPwd() {
        String password = "password1";
        assertTrue(Hashing.compareHashPass(salt, password, storedPwd));
    }
    // Test incorrect password input
    @Test
    public void incorrectPwd() {
        String password = "wrongPwd";
        assertFalse(Hashing.compareHashPass(salt, password, storedPwd));
    }

    // Test bytes to string
    @Test
    public void convertBytes() {
        byte[] test = {0, 1, 2, 3};
        assertEquals(Hashing.bytesToString(test), "AAECAw==");
    }

}
