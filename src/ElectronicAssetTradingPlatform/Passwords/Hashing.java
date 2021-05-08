package ElectronicAssetTradingPlatform.Passwords;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class Hashing {
    public static final int SALT_SIZE = 16;

    private static byte[] stringToBytes(String str) {
        return Base64.getDecoder().decode(str);
    }

    public static String bytesToString(byte[] b) {
        return Base64.getEncoder().encodeToString(b);
    }

    public static boolean compareHashPass(String salt, String password, String storedPwd) {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        md.update(stringToBytes(salt));

        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

        return Arrays.equals(stringToBytes(storedPwd), hash);
    }
}
