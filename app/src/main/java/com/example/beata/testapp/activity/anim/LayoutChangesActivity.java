package com.example.beata.testapp.activity.anim;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.beata.testapp.R;

/**
 * Created by huangbiyun on 16-12-1.
 */
public class LayoutChangesActivity extends Activity {

    private ViewGroup mContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_changes);

        mContainerView = (ViewGroup) findViewById(R.id.container);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_layout_changes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_item:
                findViewById(android.R.id.empty).setVisibility(View.GONE);
                addItem();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addItem(){
        final ViewGroup newView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.list_item_example, mContainerView, false);

        ((TextView)newView.findViewById(R.id.item_text)).setText(COUNTRIES[(int) (Math.random()*COUNTRIES.length)]);
        newView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContainerView.removeView(newView);

                if(mContainerView.getChildCount() == 0){
                    findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
                }
            }
        });

        mContainerView.addView(newView, 0);
    }

    /**
     * A static list of country names.
     */
    private static final String[] COUNTRIES = new String[]{
            "Belgium", "France", "Italy", "Germany", "Spain",
            "Austria", "Russia", "Poland", "Croatia", "Greece",
            "Ukraine",
    };
}
