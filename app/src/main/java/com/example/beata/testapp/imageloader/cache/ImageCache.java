package com.example.beata.testapp.imageloader.cache;

import android.graphics.Bitmap;

/**
 * Created by huangbiyun on 16-11-24.
 */
public abstract class ImageCache {

    public abstract Bitmap getBitmapCache(String key);

    public abstract void setBitmapCache(String url, Bitmap bitmap);

    public abstract void setMaxCacheSize(int cacheSize);

}
