package com.example.beata.testapp.activity.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.beata.testapp.R;

/**
 * Created by huangbiyun on 16-11-28.
 */
public class AnimActivity extends Activity implements View.OnClickListener{

    private View mContentView;
    private View mLoadingView;
    private int mShortAnimationDuration = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);

        mContentView = findViewById(R.id.content);
        mLoadingView = findViewById(R.id.loading_spinner);
        mContentView.setVisibility(View.GONE);

        crossfade();

        findViewById(R.id.btn_slidepage).setOnClickListener(this);
        findViewById(R.id.btn_cardflip).setOnClickListener(this);
        findViewById(R.id.btn_zoom).setOnClickListener(this);
        findViewById(R.id.btn_layoutchange).setOnClickListener(this);
    }

    private void crossfade() {
        mContentView.setAlpha(0f);
        mContentView.setVisibility(View.VISIBLE);
        mContentView.animate().alpha(1f).setDuration(mShortAnimationDuration);

        mLoadingView.animate()
                .alpha(0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoadingView.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_slidepage:
                Intent intent = new Intent(AnimActivity.this, ScreenSlidePagerActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_cardflip:
                Intent intent2 = new Intent(AnimActivity.this, CardFlipActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_zoom:
                Intent intent3 = new Intent(AnimActivity.this, ZoomActivity.class);
                startActivity(intent3);
                break;
            case R.id.btn_layoutchange:
                Intent intent4 = new Intent(AnimActivity.this, LayoutChangesActivity.class);
                startActivity(intent4);
                break;
            default:
                break;
        }
    }
}
