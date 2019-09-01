package com.zero.ci.tool;


import android.util.Base64;

import com.zero.ci.widget.logger.Logger;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description:
 * Encrypt and decrypt messages using AES 256 bit encryption that are compatible with AESCrypt-ObjC and AESCrypt Ruby.
 * -------------------------------
 * 1.
 * Encrypt
 * String password = "password";
 * String message = "hello world";
 * try {
 * String encryptedMsg = AESCrypt.encrypt(password, message);
 * }catch (GeneralSecurityException e){
 * //handle error
 * }
 * Decrypt
 * String password = "password";
 * String encryptedMsg = "2B22cS3UC5s35WBihLBo8w==";
 * try {
 * String messageAfterDecrypt = AESCrypt.decrypt(password, encryptedMsg);
 * }catch (GeneralSecurityException e){
 * //handle error - could be due to incorrect password or tampered encryptedMsg
 * }
 */
public final class AESUtil {


    //AESCrypt-ObjC uses CBC and PKCS7Padding
    private static final String AES_MODE = "AES/CBC/PKCS7Padding";
    private static final String CHARSET = "UTF-8";

    //AESCrypt-ObjC uses SHA-256 (and so a 256-bit key)
    private static final String HASH_ALGORITHM = "SHA-256";

    //AESCrypt-ObjC uses blank IV (not the best security, but the aim here is compatibility)
    private static final byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};


    /**
     * Generates SHA256 hash of the password which is used as key
     *
     * @param password used to generated key
     * @return SHA256 of the password
     */
    private static SecretKeySpec generateKey(final String password) {
        final MessageDigest digest;
        SecretKeySpec secretKeySpec = null;
        try {
            digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] bytes = new byte[0];
            bytes = password.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            byte[] key = digest.digest();
            secretKeySpec = new SecretKeySpec(key, "AES");
            //  Logger.d("SHA-256 key" + "[" + key.length + "] [" + bytesToHex(key) + "]");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return secretKeySpec;
    }


    /**
     * Encrypt and encode message using 256-bit AES with key generated from password.
     *
     * @param password used to generated key
     * @param message  the thing you want to encrypt assumed String UTF-8
     * @return Base64 encoded CipherText
     * @throws GeneralSecurityException if problems occur during encryption
     */
    public static String encrypt(final String password, String message) {

        String encoded = null;
        try {
            final SecretKeySpec key = generateKey(password);

            Logger.d("encrypt===>>> message" + "[" + message.length() + "] [" + message + "]");
            byte[] cipherText = encrypt(key, ivBytes, message.getBytes(CHARSET));

            //NO_WRAP is important as was getting \n at the end
            encoded = Base64.encodeToString(cipherText, Base64.NO_WRAP);
            Logger.d("encrypt===>>> Base64.NO_WRAP" + "[" + encoded.length() + "] [" + encoded + "]");
        } catch (UnsupportedEncodingException e) {
            Logger.e("encrypt===>>> UnsupportedEncodingException ", e);

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return encoded;
    }


    /**
     * More flexible AES encrypt that doesn't encode
     *
     * @param key     AES key typically 128, 192 or 256 bit
     * @param iv      Initiation Vector
     * @param message in bytes (assumed it's already been decoded)
     * @return Encrypted cipher text (not encoded)
     * @throws GeneralSecurityException if something goes wrong during encryption
     */
    private static byte[] encrypt(final SecretKeySpec key, final byte[] iv, final byte[] message)
            throws GeneralSecurityException {
        final Cipher cipher = Cipher.getInstance(AES_MODE);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] cipherText = cipher.doFinal(message);

        //  Logger.d("cipherText" + "[" + cipherText.length + "] [" + bytesToHex(cipherText) + "]");
        return cipherText;
    }


    /**
     * Decrypt and decode ciphertext using 256-bit AES with key generated from password
     *
     * @param password                used to generated key
     * @param base64EncodedCipherText the encrpyted message encoded with base64
     * @return message in Plain text (String UTF-8)
     * @throws GeneralSecurityException if there's an issue decrypting
     */
    public static String decrypt(final String password, String base64EncodedCipherText) {
        String message = null;
        try {
            final SecretKeySpec key = generateKey(password);
            //   Logger.d("decrypt === >>>  base64EncodedCipherText" +  base64EncodedCipherText + "]");
            byte[] decodedCipherText = Base64.decode(base64EncodedCipherText, Base64.NO_WRAP);
            //    Logger.d("decrypt === >>>  decodedCipherText"  + bytesToHex(decodedCipherText) + "]");
            byte[] decryptedBytes = decrypt(key, ivBytes, decodedCipherText);
            //    Logger.d("decrypt === >>>  decryptedBytes" + bytesToHex(decryptedBytes) + "]");
            message = new String(decryptedBytes, CHARSET);
            //     Logger.d("decrypt === >>>  message" + "[" + message.length() + "] [" + message + "]");
        } catch (UnsupportedEncodingException e) {
            Logger.e("decrypt === >>>  UnsupportedEncodingException ", e);

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return message;
    }


    /**
     * More flexible AES decrypt that doesn't encode
     *
     * @param key               AES key typically 128, 192 or 256 bit
     * @param iv                Initiation Vector
     * @param decodedCipherText in bytes (assumed it's already been decoded)
     * @return Decrypted message cipher text (not encoded)
     * @throws GeneralSecurityException if something goes wrong during encryption
     */
    private static byte[] decrypt(final SecretKeySpec key, final byte[] iv, final byte[] decodedCipherText)
            throws GeneralSecurityException {
        final Cipher cipher = Cipher.getInstance(AES_MODE);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        return cipher.doFinal(decodedCipherText);
    }


    /**
     * Converts byte array to hexidecimal useful for logging and fault finding
     */
    private static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private AESUtil() {
    }
}