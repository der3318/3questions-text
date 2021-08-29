import com.codahale.shamir.Scheme;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncryptionManager {

    private static final Scheme scheme = new Scheme(new SecureRandom(), 3, 2);

    public static String generateRandomString() {
        int a = 97, z = 122, length = Constants.RND_STR_LENGTH;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(length);
        for (int idx = 0; idx < length; idx++) {
            int rndChar = a + (int) (random.nextFloat() * (z - a + 1));
            buffer.append((char) rndChar);
        }
        return buffer.toString();
    }

    public static List<String> getThreeSplitShares(String secret) {
        Map<Integer, byte[]> parts = scheme.split(secret.getBytes(StandardCharsets.UTF_8));
        List<String> sharedKeys = new ArrayList<>();
        for (int id = 1; id <= 3; id++) {
            sharedKeys.add(new BASE64Encoder().encode(parts.get(id)));
        }
        return sharedKeys;
    }

    public static String computeSecretFromShares(int idA, String shareA, int idB, String shareB) {
        try {
            Map<Integer, byte[]> parts = new HashMap<>();
            parts.put(idA, new BASE64Decoder().decodeBuffer(shareA));
            parts.put(idB, new BASE64Decoder().decodeBuffer(shareB));
            return new String(scheme.join(parts), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "ERROR";
        }
    }

    public static String encrypt(String str, String keyStr) {
        try {
            Key key = new SecretKeySpec(ensureKeyStrLength(keyStr).getBytes(StandardCharsets.UTF_8), Constants.CIPHER_ALGOITHM);
            Cipher cipher = Cipher.getInstance(Constants.CIPHER_ALGOITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return new BASE64Encoder().encode(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public static String decrypt(String str, String keyStr) {
        try {
            Key key = new SecretKeySpec(ensureKeyStrLength(keyStr).getBytes(StandardCharsets.UTF_8), Constants.CIPHER_ALGOITHM);
            Cipher cipher = Cipher.getInstance(Constants.CIPHER_ALGOITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(new BASE64Decoder().decodeBuffer(str)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private static String ensureKeyStrLength(String keyStr) {
        return String.join(" ", Collections.nCopies(Constants.RND_STR_LENGTH + 1, keyStr)).substring(0, Constants.RND_STR_LENGTH);
    }

}
