package com.example.beata.testapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.beata.testapp.R;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

/**
 * Created by huangbiyun on 16-10-25.
 */
public class SecondActivity extends Activity implements View.OnClickListener{

    static final int PICK_CONTACT_REQUEST = 1;  // The request code
    static final int PICK_IMG_REQUEST = 2;
    private TextView phoneText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);

        phoneText = (TextView)findViewById(R.id.phone_text);


        getActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.btn_call).setOnClickListener(this);
        findViewById(R.id.btn_file).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_call:
                /*Uri number = Uri.parse("tel:5551234");
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);*/


                /*intent.setType("text/plain") ; // 真机上使用这行
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"test@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT,"subject goes here");
                intent.putExtra(Intent.EXTRA_TEXT,"body goes here");*/

               // intent.putExtra(Intent.EXTRA_STREAM, "http://m2.quanjing.com/2m/fod_liv002/fo-11171537.jpg");
                //intent.setType("image/*");

                //Intent chooser = Intent.createChooser(intent, "choose a app");

                /*pickContact();*/

                break;
            case R.id.btn_file:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMG_REQUEST);
                break;
            default:
                break;
        }
    }

    private void pickContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_CONTACT_REQUEST){
            Uri contactUri = data.getData();
            String[] queryFileds = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
            Cursor c = getContentResolver().query(contactUri, queryFileds, null, null, null);
            if(c.getCount() == 0){
                c.close();
                return;
            }
            c.moveToFirst();
            String suspect = c.getString(0);
            phoneText.setText(suspect);
            c.close();
        }else if(requestCode == PICK_IMG_REQUEST){
            Uri imgUri = data.getData();
            try {
                Cursor returnCursor = getContentResolver().query(imgUri, null, null, null, null);
                returnCursor.moveToFirst();
                String name = returnCursor.getString(returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                long size = returnCursor.getLong(returnCursor.getColumnIndex(OpenableColumns.SIZE));
                Log.d("imgTest","name = "+name+"  size = "+size);

                ParcelFileDescriptor mInputPFD = getContentResolver().openFileDescriptor(imgUri, "r");
                FileDescriptor fd = mInputPFD.getFileDescriptor();
                ImageView imageView = (ImageView)findViewById(R.id.imageView);
                imageView.setImageURI(imgUri);
                //imageView.setImageBitmap(BitmapFactory.decodeFileDescriptor(fd));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
