package com.example.vbantublooddonationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vbantublooddonationapp.databinding.ActivityAppointmentSuccessBinding;

public class AppointmentSuccess extends AppCompatActivity {

    private ActivityAppointmentSuccessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppointmentSuccessBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);


        binding.aasBtnClose.setOnClickListener(new View.OnClickListener() {
            //go back to home page
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AppointmentSuccess.this, HomeActivity.class);
                startActivity(i);
                //set slide down animation
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                finish();
            }
        });
    }
}