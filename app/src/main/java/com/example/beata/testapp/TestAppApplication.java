package com.example.beata.testapp;

import android.app.Application;

import com.example.beata.testapp.utils.CameraUtils;

/**
 * Created by huangbiyun on 16-11-22.
 */
public class TestAppApplication extends Application {

    private static TestAppApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        CameraUtils.initCameraInfo();
    }

    public static TestAppApplication getInstance(){
        return instance;
    }
}
