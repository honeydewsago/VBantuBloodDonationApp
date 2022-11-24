package com.example.vbantublooddonationapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.ActivitySettingBinding;

import java.util.List;

public class SettingActivity extends AppCompatActivity {

    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private ActivitySettingBinding binding;
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private String mUserType = "user";
    private List<User> mUserList;
    private UserViewModel mUserViewModel;
    private OrganiserViewModel mOrganiserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp", MODE_PRIVATE);
        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)) {
            mUserID = mPreferences.getInt(USERID_KEY,1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        binding.asBtnBack.setOnClickListener(new View.OnClickListener() {
            //return to profile
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.asTvUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUsertype(mUserType);
            }
        });

        binding.asTvChangePassword.setOnClickListener(new View.OnClickListener() {
            //go to change password
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this, ChangePassword.class));
            }
        });

        binding.asTvCustomerSupport.setOnClickListener(new View.OnClickListener() {
            //go to customer support
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this, CustomerSupport.class));
            }
        });

        binding.asTvLogout.setOnClickListener(new View.OnClickListener() {
            //logout
            @Override
            public void onClick(View view) {
                logout();
            }
        });

    }

    private void checkUsertype(String userType) {
        //check whether usertype is user or organiser
        if (userType.equals("organiser")) {
            startActivity(new Intent(SettingActivity.this, UpdateOrganiserProfile.class));
        }
        if (userType.equals("user")){
            startActivity(new Intent(SettingActivity.this, UpdateUserProfile.class));
        }
    }

    private void logout() {

        //set alertdialog to confirm whether user want to logout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.logout);
        builder.setMessage("Do you want to logout your account?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences mSharedPreferences = getSharedPreferences("com.example.vbantublooddonationapp", MODE_PRIVATE);
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        Intent i = new Intent(SettingActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finishAffinity();
                        startActivity(i);
                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void finish() {
        //clear the data in the shared preferences file
        SharedPreferences.Editor spEditor = mPreferences.edit();
        spEditor.clear();
        spEditor.apply();

        //clear all activity in background
        super.finish();
        //set transition animation
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}