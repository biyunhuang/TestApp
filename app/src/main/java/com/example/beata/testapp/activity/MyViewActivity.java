package com.example.beata.testapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.my.librarytutorial.MyView;

/**
 * Created by huangbiyun on 16-12-8.
 */
public class MyViewActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v = new MyView(this);
        setContentView(v);
    }
}
