package com.example.vbantublooddonationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vbantublooddonationapp.databinding.ActivityOtpactivityBinding;

public class OTPActivity extends AppCompatActivity {

    private ActivityOtpactivityBinding binding;
    private String userType;
    private String otp;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpactivityBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        Intent i = getIntent();
        userType = i.getStringExtra("userType");
        userID = i.getIntExtra("userID", 1);
        otp = i.getStringExtra("otp");

        binding.aoBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valOtp = binding.aoEtOTP.getText().toString();
                if (valOtp.equals(otp)){
                    Intent rp = new Intent(OTPActivity.this, ResetPassword.class);
                    rp.putExtra("userType", userType);
                    rp.putExtra("userID", userID);
                    startActivity(rp);
                    finish();
                }
                else{
                    Toast.makeText(OTPActivity.this, "The OTP is not correct", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}