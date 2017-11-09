package com.example.beata.testapp.utils;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by huangbiyun on 2017/10/31.
 */

public class CameraLoader {

    private static final String TAG = CameraLoader.class.getSimpleName();

    // 相机默认宽高，相机的宽度和高度跟屏幕坐标不一样，手机屏幕的宽度和高度是反过来的。
    public static final int DEFAULT_WIDTH = 1280;
    public static final int DEFAULT_HEIGHT = 720;
    public static final int DESIRED_PREVIEW_FPS = 30;

    private Activity mActivity;
    private int mCameraID;
    private Camera mCamera;
    private int mCameraPreviewFps;
    private int mOrientation = 0;

    private  int mFontCameraId = -1;
    private  int mBackCameraId = -1;
    public  boolean mUseFontCamera = true;

    private FocusCallback mMyFocusCallback;

    public interface FocusCallback {
        void onAutoFocus(boolean success);
    }

    Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback(){
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (null != mMyFocusCallback){
                mMyFocusCallback.onAutoFocus(success);
            }
        }
    };

    public CameraLoader(Activity activity, boolean useFontCamera){
        initCameraInfo();
        mUseFontCamera = useFontCamera;
        mActivity = activity;
        Log.d(TAG, "CameraLoader instance");
    }

    public void initCamera(){
        if (mUseFontCamera){
            openCamera(mFontCameraId, DESIRED_PREVIEW_FPS);
        } else {
            openCamera(mBackCameraId, DESIRED_PREVIEW_FPS);
        }
    }

    void initCameraInfo(){
        if(mFontCameraId > 0 && mBackCameraId > 0){
            return;
        }

        Camera.CameraInfo info = new Camera.CameraInfo();
        int numbers = Camera.getNumberOfCameras();
        for (int i = 0; i < numbers; i++) {
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mBackCameraId = i;
            } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                mFontCameraId = i;
            }
        }
    }

    /**
     * 根据ID打开相机
     * @param cameraID
     * @param expectFps
     */
    void openCamera(int cameraID, int expectFps) {
        if (mCamera != null) {
            throw new RuntimeException("camera already initialized!");
        }

        mCamera = Camera.open(cameraID);
        if (mCamera == null) {
            mCamera = Camera.open();
        }

        if (mCamera == null) {
            throw new RuntimeException("Unable to open camera");
        }

        mCameraID = cameraID;
        if(mCameraID == mFontCameraId){
            mUseFontCamera = true;
        }else {
            mUseFontCamera = false;
        }

        setPreviewSize(mCamera, CameraLoader.DEFAULT_WIDTH, CameraLoader.DEFAULT_HEIGHT);
        setPictureSize(mCamera, CameraLoader.DEFAULT_WIDTH, CameraLoader.DEFAULT_HEIGHT);
        calculateCameraPreviewOrientation(mActivity);
        mCamera.setDisplayOrientation(mOrientation);

        Camera.Parameters parameters = mCamera.getParameters();
        mCameraPreviewFps = chooseFixedPreviewFps(parameters, expectFps * 1000);
        parameters.setRecordingHint(true);
        List<String> modes = parameters.getSupportedFocusModes();
        if (modes.contains(Camera.Parameters.FOCUS_MODE_AUTO)){
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        mCamera.setParameters(parameters);

    }

    /**
     * 选择合适的FPS
     *
     * @param parameters
     * @param expectedThoudandFps 期望的FPS
     * @return
     */
    int chooseFixedPreviewFps(Camera.Parameters parameters, int expectedThoudandFps) {
        List<int[]> supportedFps = parameters.getSupportedPreviewFpsRange();
        for (int[] entry : supportedFps) {
            if (entry[0] == entry[1] && entry[0] == expectedThoudandFps) {
                parameters.setPreviewFpsRange(entry[0], entry[1]);
                return entry[0];
            }
        }

        int[] temp = new int[2];
        int guess;
        parameters.getPreviewFpsRange(temp);
        if (temp[0] == temp[1]) {
            guess = temp[0];
        } else {
            guess = temp[1] / 2;
        }
        return guess;
    }

    /**
     * 设置预览大小
     * @param camera
     * @param expectWidth
     * @param expectHeight
     */
    void setPreviewSize(Camera camera, int expectWidth, int expectHeight) {
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = calculatePerfectSize(parameters.getSupportedPreviewSizes(), expectWidth, expectHeight);
        parameters.setPreviewSize(size.width, size.height);
        camera.setParameters(parameters);
    }

    /**
     * 设置拍摄的照片大小
     * @param camera
     * @param expectWidth
     * @param expectHeight
     */
    void setPictureSize(Camera camera, int expectWidth, int expectHeight) {
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = calculatePerfectSize(parameters.getSupportedPictureSizes(), expectWidth, expectHeight);
        parameters.setPictureSize(size.width, size.height);
        camera.setParameters(parameters);
    }

    /**
     * 计算最完美的Size
     * @param sizes
     * @param expectWidth
     * @param expectHeight
     * @return
     */
    Camera.Size calculatePerfectSize(List<Camera.Size> sizes, int expectWidth, int expectHeight) {

        sortList(sizes);
        Camera.Size result = sizes.get(0);
        boolean widthOrHeight = false; // 判断存在宽或高相等的Size

        // 辗转计算宽高最接近的值
        for (Camera.Size size: sizes) {
            if(size.width == expectWidth && size.height == expectHeight){
                result = size;
                break;
            }

            if (size.width == expectWidth){
                widthOrHeight = true;
                if (Math.abs(result.height - expectHeight) > Math.abs(size.height - expectHeight)){
                    result = size;
                }
            } else if (size.height == expectHeight){
                widthOrHeight = true;
                if(Math.abs(result.width - expectWidth) > Math.abs(size.width - expectWidth)){
                    result = size;
                }
            } else if (!widthOrHeight) {
                if (Math.abs(result.width - expectWidth) > Math.abs(size.width - expectWidth)
                        && Math.abs(result.height - expectHeight) > Math.abs(size.height - expectHeight)) {
                    result = size;
                }
            }
        }

        return  result;
    }

    /**
     * 排序
     * @param list
     */
    void sortList(List<Camera.Size> list) {
        Collections.sort(list, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size pre, Camera.Size after) {
                if (pre.width > after.width) {
                    return 1;
                } else if (pre.width < after.width) {
                    return -1;
                }
                return 0;
            }
        });
    }

    /**
     * 切换相机
     * @param holder
     */
    public void switchCameraFace(SurfaceHolder holder){
        if(mUseFontCamera){
            switchCamera(mBackCameraId, holder);
        } else {
            switchCamera(mFontCameraId, holder);
        }
    }

    void switchCamera(int cameraID, SurfaceHolder holder) {
        if (mCameraID == cameraID) {
            return;
        }
        // 释放原来的相机
        releaseCamera();
        // 打开相机
        openCamera(cameraID, CameraLoader.DESIRED_PREVIEW_FPS);
        // 打开预览
        startPreviewDisplay(holder);
    }


    public void startPreviewDisplay(SurfaceHolder holder) {
        if (mCamera == null) {
            throw new IllegalStateException("Camera must be set when start preview");
        }
        try {
            stopPreview();
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 释放相机
     */
    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 开始预览
     */
    public void startPreview() {
        if (mCamera != null) {
            try{
                mCamera.startPreview();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止预览
     */
    public void stopPreview() {
        if (mCamera != null) {
            try{
                mCamera.stopPreview();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 拍照
     */
    public void takePicture(Camera.ShutterCallback shutterCallback,
                                   Camera.PictureCallback rawCallback,
                                   Camera.PictureCallback pictureCallback) {
        if (mCamera != null) {
            mCamera.takePicture(shutterCallback, rawCallback, pictureCallback);
        }
    }

    /**
     * 设置预览角度，setDisplayOrientation本身只能改变预览的角度
     * previewFrameCallback以及拍摄出来的照片是不会发生改变的，拍摄出来的照片角度依旧不正常的
     * 拍摄的照片需要自行处理
     * 这里Nexus5X的相机简直没法吐槽，后置摄像头倒置了，切换摄像头之后就出现问题了。
     * @param activity
     */
    public int calculateCameraPreviewOrientation(Activity activity) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraID, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        Log.d("hby", "屏幕角度 degrees = "+degrees);
        Log.d("hby", "info.orientation = "+info.orientation);

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        mOrientation = result;
        Log.d("hby", "mOrientation = "+mOrientation);
        return result;
    }

    public int calculatePictureOrientation(Activity activity){
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraID, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        Log.d("hby", "calculatePictureOrientation result = "+result);
        return result;

    }

    public void setAutoFocusCallback(FocusCallback focusCallback) {
        this.mMyFocusCallback = focusCallback;
    }

    public void onFocus(Point point) {
        if (null == mCamera){
            return;
        }

        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters.getMaxNumFocusAreas() > 0){
            List<Camera.Area> areas = new ArrayList<Camera.Area>();
            int left = point.x - 300;
            int top = point.y - 300;
            int right = point.x + 300;
            int bottom = point.y + 300;
            left = left < -1000 ? -1000 : left;
            top = top < -1000 ? -1000 : top;
            right = right > 1000 ? 1000 : right;
            bottom = bottom > 1000 ? 1000 : bottom;
            areas.add(new Camera.Area(new Rect(left, top, right, bottom), 100));
            parameters.setFocusAreas(areas);

            try{
                mCamera.setParameters(parameters);
                mCamera.autoFocus(mAutoFocusCallback);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
