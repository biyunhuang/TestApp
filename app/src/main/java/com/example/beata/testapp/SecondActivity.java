package com.example.beata.testapp;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by huangbiyun on 16-10-25.
 */
public class SecondActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
