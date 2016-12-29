package com.example.beata.testapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.SearchView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beata.testapp.Constatns;
import com.example.beata.testapp.R;
import com.example.beata.testapp.activity.anim.AnimActivity;
import com.example.beata.testapp.activity.dynamicload.DynamicLoadActivity;
import com.example.beata.testapp.activity.network.HttpExampleActivity;
import com.example.beata.testapp.service.FetchAddressIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends Activity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates = true;
    private String mAddressOutput;

    private TextView mTextView;

    private static final String REQUESTING_LOCATION_UPDATES_KEY = "location_update_key";
    private static final String LOCATION_KEY = "location_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView)findViewById(R.id.text);

        createLocationRequest();
        buildGoogleApiClient();

        setOverflowShowingAlways();
        findViewById(R.id.btn_thirdPage).setOnClickListener(this);
        findViewById(R.id.btn_bitmap).setOnClickListener(this);
        findViewById(R.id.btn_anim).setOnClickListener(this);
        findViewById(R.id.btn_network).setOnClickListener(this);
        findViewById(R.id.btn_dynamic).setOnClickListener(this);

        updateValuesFromBundle(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected() && !mRequestingLocationUpdates){
            startLocationUpdates();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        outState.putParcelable(LOCATION_KEY, mLastLocation);
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

        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        ShareActionProvider shareActionProvider = (ShareActionProvider) shareItem.getActionProvider();
        setShareIntent(shareActionProvider);

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
            case R.id.action_user:
                Intent intent2 = new Intent(this,MyViewActivity.class);
                startActivity(intent2);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_thirdPage:
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_bitmap:
                Intent intent2 = new Intent(MainActivity.this, BitmapActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_anim:
                Intent intent3 = new Intent(MainActivity.this, AnimActivity.class);
                startActivity(intent3);
                break;
            case R.id.btn_network:
                Intent intent4 = new Intent(MainActivity.this, HttpExampleActivity.class);
                startActivity(intent4);
                break;
            case R.id.btn_dynamic:
                Intent intent5 = new Intent(MainActivity.this, DynamicLoadActivity.class);
                startActivity(intent5);
                break;
            default:
                break;
        }
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

    private void setShareIntent(ShareActionProvider provider) {
        if (provider != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,"body goes here");
            Intent chooserIntent = Intent.createChooser(shareIntent, "choose app");

            provider.setShareIntent(chooserIntent);
        }
    }

    private synchronized void buildGoogleApiClient(){
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int code = apiAvailability.isGooglePlayServicesAvailable(this);

        if(code != ConnectionResult.SUCCESS && apiAvailability.isUserResolvableError(code)){
            apiAvailability.getErrorDialog(this, code, 2404).show();
        }else{
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            if(null != mGoogleApiClient){
                mGoogleApiClient.connect();  //开启连接
            }
        }

    }

    private void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1800000);
        mLocationRequest.setFastestInterval(1200000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates(){
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null){
            updateLocationInfo(mLastLocation);
        }

        if(mRequestingLocationUpdates){
            startLocationUpdates(); //开启检查更新
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mTextView.setText("get location onConnectionSuspended!");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mTextView.setText("get location error!");
    }

    @Override
    public void onLocationChanged(Location location) {
        //location 变化
        mLastLocation = location;
        updateLocationInfo(mLastLocation);
    }

    private void updateLocationInfo(Location location){
        if(null != location){
            mTextView.setText("update time : "+ DateFormat.getTimeInstance().format(new Date())+ " long :"+location.getLongitude() + "  lati :"+location.getLatitude());
            startIntentService();
        }
    }

    private void displayAddressOutput(){
        mTextView.setText(mAddressOutput);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRequestingLocationUpdates = false;
        stopLocationUpdates();
    }

    private void stopLocationUpdates(){
        if(null != mGoogleApiClient){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
            }

            // Update the value of mCurrentLocation from the Bundle and update the
            // UI to show the correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that
                // mCurrentLocationis not null.
                mLastLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            updateLocationInfo(mLastLocation);
        }
    }

    private void startIntentService(){
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constatns.LOCATION_DATA_EXTRA, mLastLocation);
        intent.putExtra(Constatns.RECEIVER, new ResultReceiver(new Handler()){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {

                // Display the address string
                // or an error message sent from the intent service.
                mAddressOutput = resultData.getString(Constatns.RESULT_DATA_KEY);
                displayAddressOutput();

                // Show a toast message if an address was found.
                if (resultCode == Constatns.SUCCESS_RESULT) {
                    Toast.makeText(MainActivity.this, "address found",Toast.LENGTH_SHORT).show();
                }

            }
        });

        startService(intent);
    }

}
