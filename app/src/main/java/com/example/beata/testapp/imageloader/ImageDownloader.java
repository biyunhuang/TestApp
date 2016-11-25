package com.example.beata.testapp.imageloader;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.beata.testapp.imageloader.cache.ImageCache;
import com.example.beata.testapp.utils.BitmapUtils;

import java.lang.ref.WeakReference;

/**
 * Created by huangbiyun on 16-11-23.
 */
public class ImageDownloader {

    private static ImageDownloader  mInstance;

    private ImageLoaderConfig imageLoaderConfig;
    private ImageCache imageCache;


    public static synchronized ImageDownloader getInstance(){
        if(null == mInstance){
            mInstance =  new ImageDownloader();
        }
        return mInstance;
    }

    public void init(ImageLoaderConfig config){
        if (config == null) {
            throw new IllegalArgumentException("ImageLoader configuration can not be initialized with null");
        }
        imageLoaderConfig = config;
        imageCache = imageLoaderConfig.imageCache;
    }

    public void download(String url, ImageView imageView) {
        checkConfiguration();
        if(cancelPotentialDownload(url, imageView)){
            Bitmap bitmap = null;
            if(imageLoaderConfig.shouldCache){
                bitmap = imageCache.getBitmapCache(url);
            }
            if(null != bitmap){
                imageView.setImageBitmap(bitmap);
            }else{
                BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
                DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
                imageView.setImageDrawable(downloadedDrawable); //持有 task 对象
                task.execute(url);
            }
        }else{
            Log.i("ImageDownloader", "已经启动task啦 不需要加载");
        }

    }

    class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private String url;
        private final WeakReference<ImageView> imageViewReference;

        public BitmapDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        // Actual download method, run in the task thread
        protected Bitmap doInBackground(String... params) {
            // params comes from the execute() call: params[0] is the url.
            Log.i("ImageDownloader", "doInBackground url  = "+params[0]);
            url = params[0];
            return BitmapUtils.downloadBitmap(url);
        }

        @Override
        // Once the image is downloaded, associates it to the imageView
        protected void onPostExecute(Bitmap bitmap) {
            if(imageLoaderConfig.shouldCache && null != bitmap){
                 imageCache.setBitmapCache(url, bitmap);
            }
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
                    // Change bitmap only if this process is still associated with it
                    if (this == bitmapDownloaderTask) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        }
    }

    static class DownloadedDrawable extends ColorDrawable {
        private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

        public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
            super(Color.GRAY);
            bitmapDownloaderTaskReference =
                    new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
        }

        public BitmapDownloaderTask getBitmapDownloaderTask() {
            return bitmapDownloaderTaskReference.get();
        }
    }

    /**
     * 和之前task url比较，如果相同则不需要重新下载
     * @param url
     * @param imageView
     * @return
     */
    private static boolean cancelPotentialDownload(String url, ImageView imageView) {

        BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
        if (bitmapDownloaderTask != null) {
            String bitmapUrl = bitmapDownloaderTask.url;
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                bitmapDownloaderTask.cancel(true);
            } else {
                // The same URL is already being downloaded.
                return false;
            }
        }
        return true;
    }

    private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }

    private void checkConfiguration() {
        if (imageLoaderConfig == null) {
            throw new IllegalStateException("ImageLoader must be init with configuration before using");
        }
    }
}
