package com.example.vbantublooddonationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.vbantublooddonationapp.databinding.ActivityBloodRequestHistoryBinding;
import com.example.vbantublooddonationapp.databinding.ActivitySingleBloodBankLocationBinding;

public class BloodRequestHistory extends AppCompatActivity {

    private ActivityBloodRequestHistoryBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityBloodRequestHistoryBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.abrhBtnMakeBloodRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BloodRequestHistory.this,MakeBloodRequest.class);
                startActivity(i);
            }
        });
    }
}