package com.example.beata.testapp.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.example.beata.testapp.R;
import com.example.beata.testapp.service.IMyAidlInterface;
import com.example.beata.testapp.service.MyService;

public class ServiceTestActivity extends ActionBarActivity implements View.OnClickListener{


    private boolean isBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_test);

        findViewById(R.id.start_service).setOnClickListener(this);
        findViewById(R.id.stop_service).setOnClickListener(this);
        findViewById(R.id.bind_service).setOnClickListener(this);
        findViewById(R.id.unbind_service).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.start_service:
                Intent startIntent = new Intent(this, MyService.class);
                startService(startIntent);
                break;
            case R.id.stop_service:
                Intent stopIntent = new Intent(this, MyService.class);
                stopService(stopIntent);
                break;
            case R.id.bind_service:
                Intent bindIntent = new Intent(this, MyService.class);
                bindService(bindIntent, connection, BIND_AUTO_CREATE);
                break;
            case R.id.unbind_service:
                unbindService(connection);
            default:
                break;
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            isBind = true;

            IMyAidlInterface myAidlService = IMyAidlInterface.Stub.asInterface(service);
            try {
                int resule = myAidlService.plus(2,3);
                String upperStr = myAidlService.toUpperCace("funk you!");
                Log.d("ServiceTestActivity","result is :"+resule);
                Log.d("ServiceTestActivity", "str is :"+upperStr);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(isBind){
            unbindService(connection);
        }
    }
}
