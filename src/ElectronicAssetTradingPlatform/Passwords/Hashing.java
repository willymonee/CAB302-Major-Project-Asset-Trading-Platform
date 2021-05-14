package ElectronicAssetTradingPlatform.Passwords;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class Hashing {
    public static final int SALT_SIZE = 12;

    private static byte[] stringToBytes(String str) {
        return Base64.getDecoder().decode(str);
    }

    public static String bytesToString(byte[] b) {
        return Base64.getEncoder().encodeToString(b);
    }

    public static boolean compareHashPass(String salt, String password, String storedPwd) {
        return Arrays.equals(stringToBytes(storedPwd), createHash(stringToBytes(salt), password));
    }

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
