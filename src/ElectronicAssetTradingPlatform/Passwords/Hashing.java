package ElectronicAssetTradingPlatform.Passwords;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * Class for creating and checking the password and its hashes
 */
public class Hashing {
    public static final int SALT_SIZE = 12;

    private static byte[] stringToBytes(String str) {
        return Base64.getDecoder().decode(str);
    }

    /**
     * Convert the byte array into string
     * @param b Byte array
     * @return The converted string
     */
    public static String bytesToString(byte[] b) {
        return Base64.getEncoder().encodeToString(b);
    }

    /**
     * Compare the given password with salt, to the stored hashed password
     * @param salt The given salt
     * @param password The inputed password
     * @param storedPwd The previously hashed password
     * @return True if the input password and stored password are equal
     */
    public static boolean compareHashPass(String salt, String password, String storedPwd) {
        return Arrays.equals(stringToBytes(storedPwd), createHash(stringToBytes(salt), password));
    }

    /**
     * Create a hash of the given password, using the given salt
     * @param salt Bytes used to hash the password
     * @param password The password to be hashed
     * @return Byte array of the hashed password
     */
    public static byte[] createHash(byte[] salt, String password) {
        // Hashing password by Sam Millington (9/1/2021) https://www.baeldung.com/java-password-hashing
        try {
            // Config hash function
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);

            // Generate hash
            return md.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create a byte array of length, usually to generate new salt
     * @param length Length of byte array
     * @return Random byte array for salt
     */
    public static byte[] newRngBytes(int length) {
        // Check length
        if (length == 0) throw new IndexOutOfBoundsException("Length cannot be 0");

        // Create random bytes
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[length];
        random.nextBytes(salt);

        return salt;
    }
}
