package com.example.beata.testapp.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.example.beata.testapp.task.BitmapWorkerTask;

import java.lang.ref.WeakReference;

/**
 * Created by huangbiyun on 16-11-23.
 */
public class AsyncDrawable extends BitmapDrawable {

    private final WeakReference bitmapWorkerTaskReference;

    public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
        super(res, bitmap);
        bitmapWorkerTaskReference =
                new WeakReference(bitmapWorkerTask);
    }

    public BitmapWorkerTask getBitmapWorkerTask() {
        return (BitmapWorkerTask)bitmapWorkerTaskReference.get();
    }

}
