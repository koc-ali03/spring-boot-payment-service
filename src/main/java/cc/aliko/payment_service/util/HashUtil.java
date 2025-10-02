package cc.aliko.payment_service.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import cc.aliko.payment_service.exception.HashGenerationException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class HashUtil {

    private static final SecureRandom RNG = new SecureRandom();

    public static String generateHashKey(String total,
                                         int installment,
                                         String currencyCode,
                                         String merchantKey,
                                         String invoiceId,
                                         String appSecret) {
        try {
            String data = String.join("|",
                    total,
                    String.valueOf(installment),
                    currencyCode,
                    merchantKey,
                    invoiceId
            );

            String ivHex = sha1Hex(String.valueOf(RNG.nextInt())).substring(0, 16);
            byte[] iv = ivHex.getBytes(StandardCharsets.UTF_8);

            String passwordHex = sha1Hex(appSecret);

            String salt = sha1Hex(String.valueOf(RNG.nextInt())).substring(0, 4);

            String saltedPasswordHex = sha256Hex(passwordHex + salt);

            byte[] keyBytes = saltedPasswordHex.getBytes(StandardCharsets.UTF_8);
            if (keyBytes.length > 32) {
                byte[] tmp = new byte[32];
                System.arraycopy(keyBytes, 0, tmp, 0, 32);
                keyBytes = tmp;
            }

            byte[] encrypted = aes256CbcEncrypt(data.getBytes(StandardCharsets.UTF_8), keyBytes, iv);
            String encryptedB64 = Base64.getEncoder().encodeToString(encrypted);

            String bundle = ivHex + ":" + salt + ":" + encryptedB64;
            return bundle.replace("/", "__");

        }
        catch (Exception e) {
            throw new HashGenerationException("Hash üretilemedi", e);
        }
    }

    private static String sha1Hex(String input) throws Exception {
        return digestHex("SHA-1", input.getBytes(StandardCharsets.UTF_8));
    }

    private static String sha256Hex(String input) throws Exception {
        return digestHex("SHA-256", input.getBytes(StandardCharsets.UTF_8));
    }

    private static String digestHex(String algorithm, byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] digest = md.digest(data);
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static byte[] aes256CbcEncrypt(byte[] plain, byte[] key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        return cipher.doFinal(plain);
    }
}
