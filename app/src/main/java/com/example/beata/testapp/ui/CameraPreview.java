package com.example.beata.testapp.ui;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.beata.testapp.activity.OpenCameraActivity;

/**
 * Created by huangbiyun on 2017/10/29.
 */

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(OpenCameraActivity.TAG, "surfaceCreated");
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(OpenCameraActivity.TAG, "surfaceDestroyed");
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mHolder.getSurface() == null){
            return;
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e){
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(OpenCameraActivity.TAG, "Error starting camera preview: " + e.getMessage());
        }
        Log.d(OpenCameraActivity.TAG, "surfaceChanged");
    }
}
