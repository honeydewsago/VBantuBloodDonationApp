package com.example.vbantublooddonationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.ActivityResetPasswordBinding;

import java.util.List;

public class ResetPassword extends AppCompatActivity {

    private ActivityResetPasswordBinding binding;
    private int userID;
    private String userType;
    private UserViewModel mUserViewModel;
    private OrganiserViewModel mOrganiserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        Intent i = getIntent();
        userType = i.getStringExtra("userType");
        userID = i.getIntExtra("userID", 1);

        binding.arpBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword(userType, userID);
            }
        });

        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mOrganiserViewModel = new ViewModelProvider(this).get(OrganiserViewModel.class);

    }

    private void updatePassword(String userType, int userID) {
        String newPassword = binding.arpEtNewPassword.getText().toString();
        String confirmNewPassword = binding.arpEtConfirmNewPassword.getText().toString();

        if (userType.equals("organiser")) {
            List<Organiser> mOrganiserList = mOrganiserViewModel.getOrganiserById(userID);
            Organiser organiser = mOrganiserList.get(0);
            if (newPassword.isEmpty()) {
                binding.arpEtNewPassword.setError(getText(R.string.passwordRequired));
                binding.arpEtNewPassword.requestFocus();
                return;
            }

            if (newPassword.length() < 8) {
                binding.arpEtNewPassword.setError(getText(R.string.passwordAtLeast8Char));
                binding.arpEtNewPassword.requestFocus();
                return;
            }

            if (confirmNewPassword.isEmpty()) {
                binding.arpEtConfirmNewPassword.setError(getText(R.string.confirmPasswordRequired));
                binding.arpEtConfirmNewPassword.requestFocus();
                return;
            }

            if (!confirmNewPassword.equals(newPassword)) {
                binding.arpEtConfirmNewPassword.setError(getText(R.string.bothPasswordDoNotMatch));
                binding.arpEtConfirmNewPassword.requestFocus();
                return;
            }

            organiser.setPassword(newPassword);
            mOrganiserViewModel.updateOrganiser(organiser);
            Toast.makeText(this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (userType.equals("user")) {
            List<User> mUserList = mUserViewModel.getUserById(userID);
            User user = mUserList.get(0);
            if (newPassword.isEmpty()) {
                binding.arpEtNewPassword.setError(getText(R.string.passwordRequired));
                binding.arpEtNewPassword.requestFocus();
                return;
            }

            if (newPassword.length() < 8) {
                binding.arpEtNewPassword.setError(getText(R.string.passwordAtLeast8Char));
                binding.arpEtNewPassword.requestFocus();
                return;
            }

            if (confirmNewPassword.isEmpty()) {
                binding.arpEtConfirmNewPassword.setError(getText(R.string.confirmPasswordRequired));
                binding.arpEtConfirmNewPassword.requestFocus();
                return;
            }

            if (!confirmNewPassword.equals(newPassword)) {
                binding.arpEtConfirmNewPassword.setError(getText(R.string.bothPasswordDoNotMatch));
                binding.arpEtConfirmNewPassword.requestFocus();
                return;
            }

            user.setPassword(newPassword);
            mUserViewModel.updateUser(user);
            Toast.makeText(this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}