package com.example.beata.testapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.beata.testapp.R;

/**
 * Created by biyun
 * on 2018/1/27.
 */

public class MyScrollerActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scroller_layout);
    }
}
