package com.example.beata.testapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.beata.testapp.R;
import com.example.beata.testapp.imageloader.ImageDownloader;
import com.example.beata.testapp.imageloader.ImageLoaderConfig;

/**
 * Created by huangbiyun on 16-11-23.
 */
public class BitmapActivity extends Activity {

    ImageView imageView;
    GridView gridView;
    String[] urlArray;

    private ImageDownloader imageDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);
        /*imageView = (ImageView)findViewById(R.id.img_sampleize);
        ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ImageLoader imageLoader = new ImageLoader();
                imageLoader.loadBitmap(imageView, R.drawable.montain, imageView.getWidth(), imageView.getHeight());
            }
        });*/

        ImageLoaderConfig config = new ImageLoaderConfig.Builder(getApplicationContext())
                .shouldCache(true)
                .setCacheType(ImageLoaderConfig.CacheType.DISK)
                .build();
        ImageDownloader.getInstance().init(config);
        imageDownloader = ImageDownloader.getInstance();

        urlArray = getResources().getStringArray(R.array.url_list);

        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter());
    }


    class ImageAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return urlArray.length;
        }

        @Override
        public Object getItem(int position) {
            return urlArray[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemViewHolder holder = null;
            if(convertView == null){
                convertView = LayoutInflater.from(BitmapActivity.this).inflate(R.layout.item_grid,null);
                holder = new ItemViewHolder();
                holder.imageView = (ImageView)convertView.findViewById(R.id.item_image);
                holder.textView = (TextView) convertView.findViewById(R.id.item_text);
                convertView.setTag(holder);
            }else{
                holder = (ItemViewHolder)convertView.getTag();
            }
            imageDownloader.download(urlArray[position],holder.imageView);
            holder.textView.setText("img-"+position);
            return convertView;
        }
    }

    class ItemViewHolder{
        public ImageView imageView;
        public TextView textView;
    }

}
