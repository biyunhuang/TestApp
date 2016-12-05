package com.example.beata.testapp.activity.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.beata.testapp.R;

/**
 * Created by huangbiyun on 16-12-5.
 */
public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(HttpExampleActivity.WIFI.equals(HttpExampleActivity.sPref)
                && networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI){

            HttpExampleActivity.refreshDisplay = true;
            Toast.makeText(context, R.string.wifi_connected, Toast.LENGTH_SHORT).show();

        }else if(HttpExampleActivity.ANY.equals(HttpExampleActivity.sPref) && networkInfo != null){

            HttpExampleActivity.refreshDisplay = true;
        }else {

            HttpExampleActivity.refreshDisplay = false;
            Toast.makeText(context, R.string.lost_connection, Toast.LENGTH_SHORT).show();
        }
    }
}
