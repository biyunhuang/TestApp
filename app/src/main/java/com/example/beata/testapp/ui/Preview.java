package com.example.beata.testapp.ui;

import android.content.Context;
import android.hardware.Camera;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.beata.testapp.activity.OpenCameraActivity;

import java.util.List;

/**
 * Created by huangbiyun on 2017/10/29.
 */

public class Preview extends FrameLayout implements SurfaceHolder.Callback {

    SurfaceView mSurfaceView;
    SurfaceHolder mHolder;
    Camera mCamera;
    List<Camera.Size> mSupportedPreviewSizes;
    Camera.Size mPreviewSize;

    public Preview(@NonNull Context context) {
        this(context, null);
    }

    public Preview(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Preview(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSurfaceView = new SurfaceView(context);
        addView(mSurfaceView);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        //mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(OpenCameraActivity.TAG, "surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        /*Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(200, 300);
        requestLayout();
        mCamera.setParameters(parameters);*/

        if (mHolder.getSurface() == null) {
            return;
        }

        // 在进行更改之前停止预览
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // 忽略：试图停止不存在的预览
        }

        // Important: Call startPreview() to start updating the preview surface.
        // Preview must be started before you can take a picture.
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(OpenCameraActivity.TAG, "surfaceChanged startPreview");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mCamera != null){
            mCamera.stopPreview();
        }
        Log.d(OpenCameraActivity.TAG, "surfaceDestroyed");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public void setCamera(Camera camera) {
        if (mCamera == camera) {
            return;
        }

        //stopPreviewAndFreeCamera();
        mCamera = camera;

        /*if (mCamera != null) {
            List<Camera.Size> sizes = mCamera.getParameters().getSupportedPreviewSizes();
            mSupportedPreviewSizes = sizes;
            if (mSupportedPreviewSizes.size() > 0) {
                mPreviewSize = mSupportedPreviewSizes.get(0);
            }
            //requestLayout();

            *//*try {
                mCamera.setPreviewDisplay(mHolder);
            } catch (Exception e) {
                e.printStackTrace();
            }

            mCamera.startPreview();*//*
            Log.d(OpenCameraActivity.TAG, "startPreview");
        }*/
    }

    private void stopPreviewAndFreeCamera() {
        if(mCamera != null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
}