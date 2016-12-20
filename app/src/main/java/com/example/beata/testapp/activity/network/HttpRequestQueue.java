package com.example.beata.testapp.activity.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by huangbiyun on 16-12-19.
 */
public class HttpRequestQueue {

    private static HttpRequestQueue mInstance;
    private RequestQueue requestQueue; //访问网络队列
    private ImageLoader mImageLoader;
    private static Context context;

    private HttpRequestQueue(Context context){
        this.context = context;
        requestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {

                    private final LruCache<String, Bitmap> cache =
                            new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String s) {
                        return cache.get(s);
                    }

                    @Override
                    public void putBitmap(String s, Bitmap bitmap) {
                        cache.put(s, bitmap);
                    }
                });
    }

    public static synchronized HttpRequestQueue getInstance(Context context){
        if(mInstance == null){
            mInstance = new HttpRequestQueue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public ImageLoader getImageLoader(){
        return mImageLoader;
    }
}
