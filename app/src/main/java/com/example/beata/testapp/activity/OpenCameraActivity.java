package com.example.beata.testapp.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.beata.testapp.R;
import com.example.beata.testapp.ui.CameraPreview;
import com.example.beata.testapp.utils.CameraLoader;
import com.example.beata.testapp.utils.FileUtils;
import com.example.beata.testapp.utils.ImageUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by huangbiyun on 2017/10/29.
 */

public class OpenCameraActivity extends Activity implements View.OnClickListener{

    public static String TAG = OpenCameraActivity.class.getSimpleName();
    private CameraPreview mCameraSurfaceView;
    private CameraLoader mCameraLoader;
    private Button mBtnTake;
    private Button mBtnSwitch;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_open_camera);

        mCameraLoader = new CameraLoader(OpenCameraActivity.this, true);
        mCameraSurfaceView = new CameraPreview(OpenCameraActivity.this, mCameraLoader);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.camera_preview);
        frameLayout.addView(mCameraSurfaceView);

        mBtnTake = (Button) findViewById(R.id.btn_take);
        mBtnTake.setOnClickListener(this);
        mBtnSwitch = (Button) findViewById(R.id.btn_switch);
        mBtnSwitch.setOnClickListener(this);

        Log.d("hby", "onCreate threadId = "+ Process.myTid());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(null != mCameraLoader){
            mCameraLoader.startPreview();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mCameraLoader){
            mCameraLoader.stopPreview();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mCameraLoader){
            mCameraLoader.releaseCamera();
            mCameraLoader = null;
        }
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take:
                takePicture();
                break;
            case R.id.btn_switch:
                switchCamera();
                break;
        }
    }

    /**
     * 拍照
     */
    private void takePicture() {
        mCameraLoader.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                mCameraLoader.stopPreview();
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                if (bitmap != null) {
                    bitmap = ImageUtils.getRotatedBitmap(bitmap, mCameraLoader.calculatePictureOrientation(OpenCameraActivity.this));
                    File path = FileUtils.getOutputMediaFile(FileUtils.MEDIA_TYPE_IMAGE);
                    try {
                        FileOutputStream fout = new FileOutputStream(path);
                        BufferedOutputStream bos = new BufferedOutputStream(fout);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        bos.flush();
                        bos.close();
                        fout.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                mCameraLoader.startPreview();
            }
        });
    }

    private void switchCamera(){
        if (null != mCameraSurfaceView) {
            mCameraLoader.switchCameraFace(mCameraSurfaceView.getHolder());
        }
    }

}
