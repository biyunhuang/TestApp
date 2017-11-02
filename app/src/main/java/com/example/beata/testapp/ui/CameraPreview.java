package com.example.beata.testapp.ui;

import android.content.Context;
import android.os.Process;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.beata.testapp.utils.CameraLoader;


/**
 * Created by huangbiyun on 2017/10/29.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = CameraPreview.class.getSimpleName();

    private SurfaceHolder mHolder;
    CameraLoader mCameraLoader;

    public CameraPreview(Context context, CameraLoader cameraLoader) {
        super(context);

        mCameraLoader = cameraLoader;
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("hby", "surfaceCreated threadId = "+Process.myTid());
        mCameraLoader.initCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCameraLoader.releaseCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        mCameraLoader.startPreviewDisplay(holder);
    }

}
