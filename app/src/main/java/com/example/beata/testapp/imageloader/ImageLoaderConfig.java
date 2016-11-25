package com.example.beata.testapp.imageloader;

import android.content.Context;

import com.example.beata.testapp.imageloader.cache.BitmapDiskCache;
import com.example.beata.testapp.imageloader.cache.BitmapMemoryCache;
import com.example.beata.testapp.imageloader.cache.ImageCache;

/**
 * Created by huangbiyun on 16-11-25.
 */
public class ImageLoaderConfig {

    public enum CacheType{
        MEMORY, DISK;
    }

    final Context context;
    final boolean shouldCache;
    final CacheType cacheType;
    final int maxCacheSize;
    final ImageCache imageCache;

    public ImageLoaderConfig(final Builder builder){
        context = builder.context;
        shouldCache = builder.shouldCache;
        cacheType = builder.cacheType;
        maxCacheSize = builder.maxCacheSize;
        imageCache = builder.imageCache;
    }

    public static class Builder{

        private Context context;
        private boolean shouldCache;
        private CacheType cacheType;
        private int maxCacheSize;

        private ImageCache imageCache;

        public Builder(Context context){
            this.context = context;
        }

        public Builder shouldCache(boolean shouldCache){
            this.shouldCache = shouldCache;
            return this;
        }

        public Builder setCacheType(CacheType type){
            this.cacheType = type;
            return this;
        }

        public Builder cacheSize(int size){
            this.maxCacheSize = size;
            return this;
        }

        public Builder setImageCache(ImageCache cache){
            this.imageCache = cache;
            return this;
        }

        public ImageLoaderConfig build(){
            initEmptyFieldsWithDefaultValues();
            return new ImageLoaderConfig(this);
        }

        private void initEmptyFieldsWithDefaultValues() {
            if(null == imageCache){
                if(cacheType == null){
                    imageCache = new BitmapMemoryCache(maxCacheSize);
                }else{
                    switch (cacheType){
                        case MEMORY:
                            imageCache = new BitmapMemoryCache(maxCacheSize);
                            break;
                        case DISK:
                            imageCache = new BitmapDiskCache(maxCacheSize);
                            break;
                        default:
                            imageCache = new BitmapMemoryCache(maxCacheSize);
                            break;
                    }
                }
            }
        }
    }
}
