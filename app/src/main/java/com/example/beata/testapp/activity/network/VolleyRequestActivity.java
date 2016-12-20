package com.example.beata.testapp.activity.network;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.beata.testapp.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangbiyun on 16-12-19.
 */
public class VolleyRequestActivity extends Activity{

    private TextView textView;
    private ImageView imageView;
    public static final String TAG = "MyTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        textView = (TextView) findViewById(R.id.text);
        imageView = (ImageView) findViewById(R.id.img_net);

        //loadText();
        //loadImage();
        loadJson();
    }

    private void loadText(){
        String url ="http://www.baidu.com";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        textView.setText("Response is : " + s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        textView.setText("it didn't work!");
                    }
                });
        stringRequest.setTag(TAG);
        HttpRequestQueue.getInstance(this).getRequestQueue().add(stringRequest);
    }

    private void loadImage(){
        String url = "http://i.imgur.com/7spzG.png";
        /*ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {

                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_INSIDE,null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        imageView.setImageResource(R.drawable.thumb1);
                    }
                });
        HttpRequestQueue.getInstance(this).getRequestQueue().add(request);*/

        ImageLoader loader = HttpRequestQueue.getInstance(this).getImageLoader();
        loader.get(url, ImageLoader.getImageListener(imageView, R.drawable.thumb1, R.drawable.thumb1));
    }

    private void loadJson(){
        String url = "http://api.ad.leomaster.com/sdkconfig";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        textView.setText("Response: " + jsonObject.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        textView.setText("error ko");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<String, String>();
                header.put("android_id", "sdfsdfdsxg35");
                header.put("appkey", "BM2000");
                return header;
            }
        };
        HttpRequestQueue.getInstance(this).getRequestQueue().add(jsObjRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
