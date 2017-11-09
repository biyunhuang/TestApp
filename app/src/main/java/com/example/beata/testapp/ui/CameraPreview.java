package com.example.beata.testapp.ui;

import android.content.Context;
import android.graphics.Point;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.beata.testapp.utils.CameraLoader;


/**
 * Created by huangbiyun on 2017/10/29.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = CameraPreview.class.getSimpleName();

    private SurfaceHolder mHolder;
    CameraLoader mCameraLoader;


    OnTouchListener mOnTouchListener = new OnTouchListener() {

        long downTime;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    downTime = SystemClock.uptimeMillis();
                    break;
                case MotionEvent.ACTION_UP:
                    if ((SystemClock.uptimeMillis() - downTime) < 500){
                        if (null != mCameraLoader) {
                            Point point = new Point((int) event.getX(), (int) event.getY());
                            mCameraLoader.onFocus(point);
                            Log.d(TAG,"onFocus");
                        }
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    public CameraPreview(Context context, CameraLoader cameraLoader) {
        super(context);

        mCameraLoader = cameraLoader;
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("hby", "surfaceCreated threadId = "+Process.myTid());
        if (null != mCameraLoader){
            mCameraLoader.initCamera();
            setOnTouchListener(mOnTouchListener);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (null != mCameraLoader){
            mCameraLoader.releaseCamera();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (null != mCameraLoader){
            mCameraLoader.startPreviewDisplay(holder);
        }
    }

}
