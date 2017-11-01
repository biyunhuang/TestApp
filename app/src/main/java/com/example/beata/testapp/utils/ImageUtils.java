package com.example.beata.testapp.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by huangbiyun on 2017/10/31.
 */

public class ImageUtils {

    public static Bitmap getRotatedBitmap(Bitmap bitmap, int orientation){

        if (null == bitmap){
            return  null;
        }

        if(orientation == 0){
            return  bitmap;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(orientation);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();
        return newBitmap;
    }
}
