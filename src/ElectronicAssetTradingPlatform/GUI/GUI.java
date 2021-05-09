package ElectronicAssetTradingPlatform.GUI;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GUI
{
    public static void loginForm() {
        // Get input from form
        String username = "user1";
        String password = "password1";

        // Match test
        boolean pwdMatch = decrypt(username, password);
        System.out.println("Should match: " + pwdMatch);

        // Not match test
        pwdMatch = decrypt(username, "password");
        System.out.println("Should not match: " + pwdMatch);
    }
    public static void createITAdminGUI() {

    }
    public static void memberGUI() {

    }
    public static void leaderGUI() {

    }
    public static void systemAdminGUI() {

    }

    // list of private GUI methods ...
    private static boolean decrypt(String username, String password) {
        // Query for the salt and password
        String storedPwd = "5ce65a89"; // Test using online result https://8gwifi.org/pbkdf.jsp
        String saltStr = "7cc355be";

        // Get salt
        byte[] salt = saltStr.getBytes();

        System.out.println("Salt: " + salt);

        // Hashing password by Sam Millington (9/1/2021) https://www.baeldung.com/java-password-hashing
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);

            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

            System.out.println("Hash: " + hash);
            System.out.println("Stored: " + storedPwd);
            // Check hash with stored hash
            return hash.equals(storedPwd);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Wrong Algorithm");
        }

        // Else
        return false;
    }
}
