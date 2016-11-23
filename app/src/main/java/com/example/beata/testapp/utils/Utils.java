package com.example.beata.testapp.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * Created by huangbiyun on 16-8-24.
 */
public class Utils {

    public static String encryptSHA1(String data){
        try {
            return byteToSHA1(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String byteToSHA1(byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(bytes);
            byte[] sha1Bytes = digest.digest();
            return convertToHexString(sha1Bytes);
        } catch (Exception e) {
            return null;
        } finally {
        }
    }

    /**
     * Convert the hash bytes to hex digits string
     * @param hashBytes
     * @return
     */
    public static String convertToHexString(byte[] hashBytes) {
        String returnVal = "";
        for (int i = 0; i < hashBytes.length; i++) {
            returnVal += Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1);
        }
        return returnVal;
    }
}
