package com.example.beata.testapp.service;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.beata.testapp.R;
import com.example.beata.testapp.activity.MainActivity;

/**
 * Created by huangbiyun on 17-1-3.
 */
public class MyService extends Service {

    public static final String TAG = "MyService";

    private MyBinder myBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");

        //使服务在前台运行
        startForeground(1, getNotification(getApplicationContext()));

        startPhantomService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand()");

        //可以创建子线程执行耗时任务

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        return myBinder;
    }

    public static synchronized Notification getNotification(Context context){

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification =  new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("有通知嘞")
                .setContentTitle("通知")
                .setContentText("内容")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOngoing(true)
                .build();

        return notification;
    }

    private void startPhantomService(){
        startService(new Intent(this, PhantomService.class));
    }

    /**
     * 和外界交互入口
     */
    public class MyBinder extends Binder{

        public void startDowndload(){
            //可以创建子线程执行耗时任务

            Log.d(TAG, "startDownload()");
        }

    }
}
