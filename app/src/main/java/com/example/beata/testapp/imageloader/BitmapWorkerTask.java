package com.example.beata.testapp.imageloader;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.beata.testapp.R;
import com.example.beata.testapp.TestAppApplication;
import com.example.beata.testapp.utils.BitmapUtils;

import java.lang.ref.WeakReference;

/**
 * Created by huangbiyun on 16-11-22.
 */
public class BitmapWorkerTask extends AsyncTask {

    private final WeakReference imageViewReference;
    private int resImgId = 0;
    private int reqWidth = 100;
    private int reqHeight = 100;

    public BitmapWorkerTask(ImageView imageView){
        imageViewReference = new WeakReference(imageView);
    }

    public BitmapWorkerTask(ImageView imageView, int reqWidth, int reqHeight){
        imageViewReference = new WeakReference(imageView);
        this.reqWidth = reqWidth;
        this.reqHeight = reqHeight;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        resImgId = (Integer)params[0];
        int resId = resImgId == 0? R.drawable.montain : resImgId;
        return BitmapUtils.decodeSampledBitmapFromResource(TestAppApplication.getInstance().getResources(), resId, reqWidth, reqHeight);
    }
    // Once complete
    @Override
    protected void onPostExecute(Object o) {
        Bitmap bitmap = (Bitmap)o;
        if(imageViewReference != null && bitmap != null){
            ImageView imageView = (ImageView) imageViewReference.get();
            if(imageView != null){
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public int getResImgId(){
        return resImgId;
    }


}
