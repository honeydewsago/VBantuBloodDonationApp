package com.example.vbantublooddonationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.vbantublooddonationapp.databinding.ActivitySingleBloodBankLocationBinding;
import com.example.vbantublooddonationapp.databinding.ActivitySingleBloodRequestBinding;

public class SingleBloodRequestActivity extends AppCompatActivity {

    private ActivitySingleBloodRequestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingleBloodRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}