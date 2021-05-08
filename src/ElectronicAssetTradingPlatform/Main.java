package ElectronicAssetTradingPlatform;

import ElectronicAssetTradingPlatform.GUI.GUI;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {
	    // write your code here
        SecureRandom random = new SecureRandom();
        //byte[] salt = new byte[16];
        byte[] salt = new byte[] {1,0,1,0,0,0,1,1,0,0,1,0,0,1,0,1,1,1,1,0,1,0,0,1,0,1};
        random.nextBytes(salt);
        System.out.println("Salt: " + salt);

        byte[] str = new BigInteger("28c97a5", 16).toByteArray();

        System.out.println("Salt: " + str);

        String password = "password1";

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);

        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

        System.out.println("Hash: " + hash);
    }
}
