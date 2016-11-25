package com.example.beata.testapp.imageloader;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.example.beata.testapp.TestAppApplication;
import com.example.beata.testapp.ui.AsyncDrawable;

/**
 * Created by huangbiyun on 16-11-23.
 */
public class ImageLoader {

    public void loadBitmap(ImageView imageView, int resId, int reqWidth, int reqHeight){
        if (cancelPotentialWork(resId, imageView)) {
            BitmapWorkerTask task = new BitmapWorkerTask(imageView, reqWidth, reqHeight);
            AsyncDrawable asyncDrawable = new AsyncDrawable(TestAppApplication.getInstance().getResources(), null, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(resId);
        }
    }

    private  boolean cancelPotentialWork(int data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final int bitmapData = bitmapWorkerTask.getResImgId();
            if (bitmapData == 0 || bitmapData != data) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }
}
