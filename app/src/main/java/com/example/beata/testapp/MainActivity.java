package com.example.beata.testapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView)findViewById(R.id.text);

        double[] local = DeviceInfoUtil2.getLocation(this);
        String msg = "imei = "+DeviceInfoUtil2.getImei(this)+"\n"
                +"mac = "+DeviceInfoUtil2.getMacAddress(this)+"\n"
                +"androidid = "+DeviceInfoUtil2.getAndroidId(this)+"\n"
                +"ip = "+DeviceInfoUtil2.getIpAddresses(this)+"\n"
                +"operation = "+DeviceInfoUtil2.getOperator(this)+"\n"
                +"netType = "+DeviceInfoUtil.getNetworkStatus(this)+"\n"
                +"make = "+DeviceInfoUtil2.getVendor()+"\n"
                +"model = "+DeviceInfoUtil2.getPhoneDeviceModel()+"\n"
                +"os version = "+DeviceInfoUtil2.getAndroidVersion()+"\n"
                +"location = "+local[0]+"---"+local[1]+"\n"
                +"resolution = "+DeviceInfoUtil2.getResolution(this)+"\n"
                +"versionName = "+DeviceInfoUtil2.getVersionName(this);
        textView.setText(msg);

        setOverflowShowingAlways();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)searchItem.getActionView();
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                Toast.makeText(this,"search clcik",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_like:
                /*Toast.makeText(this,"settings clcik",Toast.LENGTH_SHORT).show();*/
                Intent intent = new Intent(this,SecondActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
