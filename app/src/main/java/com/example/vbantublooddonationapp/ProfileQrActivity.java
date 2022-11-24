package com.example.vbantublooddonationapp;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.vbantublooddonationapp.databinding.ActivityProfileQrBinding;

import java.util.Objects;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ProfileQrActivity extends AppCompatActivity {
    //Initialize variable
    private Toolbar toolbar;
    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private String mUserType = "user";

    private ActivityProfileQrBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding with the layout xml file
        binding = ActivityProfileQrBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        //setting content view
        setContentView(v);

        //getting toolbar with binding
        toolbar = binding.apqrToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        //shared pref
        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp", MODE_PRIVATE);
        //getting values stored in shared pref
        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)){
            mUserID = mPreferences.getInt(USERID_KEY, 1);
        }
        String data = String.valueOf(mUserID);
        //encoding the data into bitmap
        QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 750);

        Bitmap qrbits = qrgEncoder.getBitmap(0);
        //setting the image view to bitmap
        binding.apqrIvQRCode.setImageBitmap(qrbits);
    }

    //back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}