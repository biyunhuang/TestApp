package com.example.beata.testapp.activity;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.FrameLayout;

import com.example.beata.testapp.R;
import com.example.beata.testapp.ui.CameraPreview;
import com.example.beata.testapp.ui.Preview;

import java.util.List;

/**
 * Created by huangbiyun on 2017/10/29.
 */

public class OpenCameraActivity extends Activity {

    public static String TAG = OpenCameraActivity.class.getSimpleName();

    CameraPreview mPreview;
    Camera mCamera;
    Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_camera);

        mHandler = new Handler(Looper.myLooper());

        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
