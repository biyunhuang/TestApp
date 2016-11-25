package com.example.beata.testapp.imageloader.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by huangbiyun on 16-11-24.
 */
public class BitmapMemoryCache extends ImageCache {

    private LruCache<String, Bitmap> mMemoryCache;

    public BitmapMemoryCache(int cacheSize){
        int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024); //kb
        if(cacheSize <= 0){
            cacheSize = maxMemory/8;
        }

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    @Override
    public Bitmap getBitmapCache(String key) {
        return getBitmapFromMemCache(key);
    }

    @Override
    public void setBitmapCache(String url, Bitmap bitmap) {
        addBitmapToMemoryCache(url, bitmap);
    }

    @Override
    public void setMaxCacheSize(int cacheSize) {

    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        synchronized (mMemoryCache) {
            if (mMemoryCache.get(key) == null) {
                mMemoryCache.put(key, bitmap);
            }
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        synchronized (mMemoryCache) {
            return mMemoryCache.get(key);
        }
    }

}
