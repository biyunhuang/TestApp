package com.example.beata.testapp.utils;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by huangbiyun on 16-12-1.
 */
public class DownloadUtils {

    private static String TAG = "DownloadUtils";

    public static InputStream downloadNetworkFile(String urlStr){
        InputStream inputStream = null;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true); //是否从httpUrlConnection读入
            connection.connect();

            int response = connection.getResponseCode();
            connection.getContentType();
            if (response == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
            }
        }catch (IOException e){
            Log.e(TAG,e.getMessage());
        }

        return inputStream;
    }
}
