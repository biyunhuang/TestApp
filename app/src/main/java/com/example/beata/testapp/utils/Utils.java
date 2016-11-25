package com.example.beata.testapp.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;

import com.example.beata.testapp.TestAppApplication;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public static String encryptMD5(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = convertToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
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

    /**
     * 返回  /sdcard/Android/data/<application package>/cache  目录下
     * @param dir
     * @return
     */
    public static File getDiskCacheDir(String dir) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !Environment.isExternalStorageRemovable() ? TestAppApplication.getInstance().getExternalCacheDir().getPath() :
                        TestAppApplication.getInstance().getCacheDir().getPath();

        return new File(cachePath + File.separator + dir);
    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }


}
