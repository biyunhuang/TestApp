package com.example.beata.testapp.activity.network;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.beata.testapp.R;
import com.example.beata.testapp.utils.DownloadUtils;
import com.example.beata.testapp.utils.Utils;

import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by huangbiyun on 16-12-1.
 */
public class HttpExampleActivity extends Activity {

    private static final String DEBUG_TAG = "NetworkStatusExample";

    private static String URL = "http://img2.imgtn.bdimg.com/it/u=1320686180,3651175792&fm=21&gp=0.jpg";

    TextView netTipView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_http);
        netTipView = (TextView) findViewById(R.id.text_network);
        imageView = (ImageView) findViewById(R.id.img_net);

        httpHandler();
    }

    private void httpHandler(){
        if(Utils.isNetworkAvaiable(this)){
            new DownloadWebImage(imageView).execute(URL);
        }else{
            netTipView.setText("网络不可用哦");
        }
    }


    private class DownloadWebImage extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;

        public DownloadWebImage(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            Bitmap bitmap = null;
            InputStream inputStream = DownloadUtils.downloadNetworkFile(url);
            if(null != inputStream){
                try{
                    bitmap = BitmapFactory.decodeStream(inputStream);
                }catch (Exception e){
                    Log.e(DEBUG_TAG, e.getMessage());
                }
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (null != bitmap && imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (null != imageView) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
