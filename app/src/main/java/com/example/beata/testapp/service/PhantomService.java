
package com.example.beata.testapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class PhantomService extends Service {

    private static final String TAG = "PhantomService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "PhantomService start");
        startForeground(1, MyService.getNotification(getApplicationContext()));
        stopSelf();
        return Service.START_NOT_STICKY;
    }

}
