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
    private Toolbar toolbar;
    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private String mUserType = "user";

    private ActivityProfileQrBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileQrBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        toolbar = binding.apqrToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp", MODE_PRIVATE);

        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)){
            mUserID = mPreferences.getInt(USERID_KEY, 1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }
        String data = String.valueOf(mUserID);
        QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 750);

        Bitmap qrbits = qrgEncoder.getBitmap(0);
        binding.apqrIvQRCode.setImageBitmap(qrbits);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}