package com.example.beata.testapp.activity;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.VideoView;

import com.example.beata.testapp.R;

import java.io.IOException;

/**
 * Created by beata on 2017/6/26.
 */

public class VideoActivity extends Activity {

    VideoView videoView;
    MediaPlayer mediaPlayer;
    SurfaceView surfaceView;
    SurfaceHolder holder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video);
        videoView = (VideoView) findViewById(R.id.video_view);

        surfaceView = (SurfaceView)findViewById(R.id.surface_view);
        holder = surfaceView.getHolder();

        //playVideo();
        //playVideoPathAsset();
    }

    @Override
    protected void onStart() {
        super.onStart();
        playVideoPathAsset();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void playVideo(){
        videoView.setVideoURI(Uri.parse("android.resource://com.example.beata.testapp/" + R.raw.home_video));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });
        videoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.stopPlayback();
    }

    private void playVideoPathAsset(){
        AssetManager assetManager = getAssets();
        try {
            AssetFileDescriptor fileDescriptor = assetManager.openFd("home_video.mp4");
            mediaPlayer = new MediaPlayer();
            //SurfaceHolder holder = videoView.getHolder();

            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.setDisplay(holder);
                    mp.start();
                    mp.setLooping(true);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
