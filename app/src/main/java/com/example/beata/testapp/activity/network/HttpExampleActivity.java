package com.example.beata.testapp.activity.network;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
public class HttpExampleActivity extends Activity implements View.OnClickListener{

    private static final String DEBUG_TAG = "NetworkStatusExample";
    private static String URL = "http://img2.imgtn.bdimg.com/it/u=1320686180,3651175792&fm=21&gp=0.jpg";

    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    private static final String PAGE_URL = "http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest";

    private static boolean wifiConnected = false;
    private static boolean mobileConnected = false;
    // The user's current network preference setting.
    public static String sPref = null;
    // Whether the display should be refreshed.
    public static boolean refreshDisplay = true;

    // The BroadcastReceiver that tracks network connectivity changes.
    private NetworkReceiver receiver;

    TextView netTipView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_http);
        netTipView = (TextView) findViewById(R.id.text_network);
        imageView = (ImageView) findViewById(R.id.img_net);

        //注册跟踪网络变化
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        registerReceiver(receiver, filter);

        findViewById(R.id.btn_volley).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //获取用户选择网络类型
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        sPref = preferences.getString("listPref", "Wi-Fi");

        //检查更新网络连接情况
        updateConnectedFlags();

        //网络变化，或者用户设置改变时 更新页面
        if(refreshDisplay){
            loadPage();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_network_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.refresh:
                loadPage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateConnectedFlags(){
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = manager.getActiveNetworkInfo();
        if(activeInfo != null && activeInfo.isConnected()){
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        }else{
            wifiConnected = false;
            mobileConnected = false;
        }
    }

    private void loadPage(){
        if((sPref.equals(ANY) && (wifiConnected || mobileConnected))
                || ((sPref.equals(WIFI) && wifiConnected))){
            new DownloadWebImage(imageView).execute(URL);
            netTipView.setText("");
        }else {
            netTipView.setText("网络不可用哦");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != receiver){
            unregisterReceiver(receiver);
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_volley:
                startActivity(new Intent(HttpExampleActivity.this, VolleyRequestActivity.class));
                break;
            default:
                break;
        }
    }
}
