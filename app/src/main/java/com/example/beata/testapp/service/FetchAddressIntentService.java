package com.example.beata.testapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.example.beata.testapp.Constatns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by huangbiyun on 16-12-21.
 */
public class FetchAddressIntentService extends IntentService {

    private static String TAG = FetchAddressIntentService.class.getSimpleName();

    protected ResultReceiver mReceiver;

    public FetchAddressIntentService(){
        this("");
    }

    public FetchAddressIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";
        Location location = intent.getParcelableExtra(Constatns.LOCATION_DATA_EXTRA);
        mReceiver = intent.getParcelableExtra(Constatns.RECEIVER);
        List<Address> addresses = null;

        Geocoder geocoder = new Geocoder(this.getApplicationContext(), Locale.getDefault());
        if(geocoder != null){
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);  //获取地址
            } catch (IOException e) {
                errorMessage = "service not available";
                Log.e(TAG, errorMessage, e);
            } catch (IllegalArgumentException e1){
                errorMessage = "invalid long used";
                Log.e(TAG, errorMessage + ". " +
                        "Latitude = " + location.getLatitude() +
                        ", Longitude = " +
                        location.getLongitude(), e1);
            }
        }

        if(addresses == null || addresses.size() == 0){
            if (errorMessage.isEmpty()) {
                errorMessage = "no address found";
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constatns.FAILURE_RESULT, errorMessage);
        }else{
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            for(int i = 0; i < address.getMaxAddressLineIndex(); i++){
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG, "address found");
            deliverResultToReceiver(Constatns.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments));
        }
    }

    private void deliverResultToReceiver(int code, String message){
        Bundle bundle = new Bundle();
        bundle.putString(Constatns.RESULT_DATA_KEY, message);
        mReceiver.send(code, bundle);
    }
}
