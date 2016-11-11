package com.example.beata.testapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;

import java.io.File;

/**
 * Created by huangbiyun on 16-11-11.
 */
public class ThirdActivity extends Activity {

    NfcAdapter mNfcAdapter;
    boolean mAndroidBeamAvailable = false;
    private Uri[] mFileUris = new Uri[1];
    private FileUriCallback mFileUriCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        initFile();

        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC)){
            mAndroidBeamAvailable = false;
        }else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1){
            mAndroidBeamAvailable = false;
        }else{
            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        }
        mFileUriCallback = new FileUriCallback();
        mNfcAdapter.setBeamPushUrisCallback(mFileUriCallback,this);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private class FileUriCallback implements NfcAdapter.CreateBeamUrisCallback {
        public FileUriCallback() {
        }
        /**
         * Create content URIs as needed to share with another device
         */
        @Override
        public Uri[] createBeamUris(NfcEvent event) {
            return mFileUris;
        }
    }

    private void initFile(){
        File extDir = getExternalFilesDir(null);
        File requestFile = new File(extDir, "dog.jpg");
        requestFile.setReadable(true, false);
        Uri fileUri = Uri.fromFile(requestFile);
        if(fileUri != null){
            mFileUris[0] = fileUri;
        }
    }

}
