package com.raywu.investingsimulator.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@Component
public class SecurityCipher {
    private static String KEY_VALUE;
    private static SecretKeySpec secretKey;
    private static byte[] key;

    private SecurityCipher() {
        throw new AssertionError("Static!");
    }

    @Autowired
    public SecurityCipher(EnvVariable env) {
        this.KEY_VALUE = env.SECURITY_CIPHER_KEY();
    }

    public static void setKey() {
        MessageDigest sha;
        try {
            key = KEY_VALUE.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String strToEncrypt) {
        if (strToEncrypt == null) return null;

        System.out.println("token to be encrypt-------" + strToEncrypt);

        try {
            setKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder()
                    .withoutPadding()
                    .encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String decrypt(String strToDecrypt) {
        System.out.println("strToDecrypt-------" + strToDecrypt);

        if (strToDecrypt == null) return null;

        try {
            setKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // the tokenProvider will throw the invalid token error if this decrypted token is null
        return null;
    }
}
