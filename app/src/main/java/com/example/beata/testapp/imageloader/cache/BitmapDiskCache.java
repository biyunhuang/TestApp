package com.example.beata.testapp.imageloader.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.beata.testapp.TestAppApplication;
import com.example.beata.testapp.utils.DiskLruCache;
import com.example.beata.testapp.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by huangbiyun on 16-11-24.
 */
public class BitmapDiskCache extends ImageCache {

    private DiskLruCache mDiskLruCache;

    private final Object mDiskCacheLock = new Object();
    private boolean mDiskCacheStarting = true;
    private int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    private String DISK_CACHE_SUBDIR = "thumbnails";

    public BitmapDiskCache(int cacheSize){
        if(cacheSize > 0){
            DISK_CACHE_SIZE = cacheSize;
        }
        File cacheDir = Utils.getDiskCacheDir(DISK_CACHE_SUBDIR);
        new InitDiskCacheTask().execute(cacheDir);
    }

    @Override
    public Bitmap getBitmapCache(String key) {
        return getBitmapFromDiskCache(key);
    }

    @Override
    public void setBitmapCache(String url, Bitmap bitmap) {
        addBitmapToDisk(url, bitmap);
    }

    @Override
    public void setMaxCacheSize(int cacheSize) {

    }


    class InitDiskCacheTask extends AsyncTask<File, Void, Void> {
        @Override
        protected Void doInBackground(File... params) {
            synchronized (mDiskCacheLock) {
                File cacheDir = params[0];
                try {
                    mDiskLruCache = DiskLruCache.open(cacheDir, Utils.getAppVersion(TestAppApplication.getInstance()), 1, DISK_CACHE_SIZE);
                    mDiskCacheStarting = false; // Finished initialization
                    mDiskCacheLock.notifyAll(); // Wake any waiting threads
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    /**
     * default thumbnails
     * @param dir
     */
    public void setCacheSubDir(String dir){
        this.DISK_CACHE_SUBDIR = dir;
    }

    private void addBitmapToDisk(String key, Bitmap bitmap) {
        // Also add to disk cache
        synchronized (mDiskCacheLock) {
            DiskLruCache.Editor editor = null;
            // Wait while disk cache is started from background thread
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {
                }
            }
            try {
                String keyMD5 = Utils.encryptMD5(key);
                if (mDiskLruCache != null && mDiskLruCache.get(keyMD5) == null) {
                    editor = mDiskLruCache.edit(keyMD5);
                    if(editor != null){
                        OutputStream outputStream = editor.newOutputStream(0);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                        outputStream.write(baos.toByteArray(),0,baos.size());
                        editor.commit();
                        mDiskLruCache.flush();
                    }
                }
            } catch (IOException e) {
                if(null != editor){
                    try {
                        editor.abort();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    private Bitmap getBitmapFromDiskCache(String key) {
        synchronized (mDiskCacheLock) {
            // Wait while disk cache is started from background thread
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {
                }
            }
            if (mDiskLruCache != null) {
                try {
                    String keyMD5 = Utils.encryptMD5(key);
                    DiskLruCache.Snapshot snapShot = mDiskLruCache.get(keyMD5);
                    if (snapShot != null) {
                        InputStream is = snapShot.getInputStream(0);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        return bitmap;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
