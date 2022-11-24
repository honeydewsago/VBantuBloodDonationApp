package com.example.vbantublooddonationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.vbantublooddonationapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        //set splash screen when app launch
        Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
        binding.appLabel.startAnimation(slideUp);

        //display splash screen for 3 seconds before proceeding to login page
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }, 3000);
    }
}