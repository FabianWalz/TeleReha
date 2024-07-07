package ehealth.telereha.utils;

import org.springframework.data.util.Pair;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashValueGenerator {
    public static Pair<String, String>  calculateHashedValue(String pw) throws NoSuchAlgorithmException {
        String salt = getSalt();
        return Pair.of(getHashedPassword(pw, salt), salt);
    }

    private static String getHashedPassword(String passwordToHash,
                                            String salt) throws NoSuchAlgorithmException {
        String generatedPassword = null;
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt.getBytes());
        byte[] bytes = md.digest(passwordToHash.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        generatedPassword = sb.toString();

        return generatedPassword;
    }

    // Add salt
    private static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }

    public static Pair<String, String> calculateHashedValue(String pw, String salt) throws NoSuchAlgorithmException {
        return Pair.of(getHashedPassword(pw, salt), salt);
    }
}
