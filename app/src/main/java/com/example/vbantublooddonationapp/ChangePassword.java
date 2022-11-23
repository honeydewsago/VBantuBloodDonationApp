package com.example.vbantublooddonationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.ActivityChangePasswordBinding;

import java.util.List;
import java.util.Objects;

public class ChangePassword extends AppCompatActivity {
    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private String mUserType = "user";
    private List<User> mUserList;
    private List<Organiser> mOrganiserList;

    private UserViewModel mUserViewModel;
    private OrganiserViewModel mOrganiserViewModel;
    private ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        //setup toolbar
        Toolbar toolbar = binding.acpTbToolBar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mOrganiserViewModel = new ViewModelProvider(this).get(OrganiserViewModel.class);

        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp", MODE_PRIVATE);
        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)){
            mUserID = mPreferences.getInt(USERID_KEY, 1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        binding.acpTvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChangePassword.this, ForgotPasswordActivity.class));
                finish();
            }
        });

        binding.acpBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });
    }

    private void updatePassword() {

        String userType = mUserType;
        int userID = mUserID;
        String oldPassword = binding.acpEtOldPassword.getText().toString();
        String newPassword = binding.acpEtNewPassword.getText().toString();
        String confirmNewPassword = binding.acpEtConfirmNewPassword.getText().toString();

        if (userType.equals("organiser")){
            List<Organiser> mOrganiserList = mOrganiserViewModel.getOrganiserById(userID);
            Organiser organiser = mOrganiserList.get(0);
            String currentPassword = organiser.getPassword();
            Log.d("TAG", "organiser changePassword: " + userID);

            if (oldPassword.isEmpty()){
                binding.acpEtOldPassword.setError(getText(R.string.passwordRequired));
                binding.acpEtOldPassword.requestFocus();
                return;
            }

            if (!oldPassword.equals(currentPassword)){
                binding.acpEtOldPassword.setError(getText(R.string.incorrectPassword));
                binding.acpEtOldPassword.requestFocus();
                return;
            }

            if (newPassword.equals(oldPassword)){
                binding.acpEtNewPassword.setError(getText(R.string.userOtherPassword));
                binding.acpEtNewPassword.requestFocus();
                return;
            }

            if (newPassword.isEmpty()){
                binding.acpEtNewPassword.setError(getText(R.string.passwordRequired));
                binding.acpEtNewPassword.requestFocus();
                return;
            }

            if (newPassword.length()<8){
                binding.acpEtNewPassword.setError(getText(R.string.passwordAtLeast8Char));
                binding.acpEtNewPassword.requestFocus();
                return;
            }

            if (confirmNewPassword.isEmpty()){
                binding.acpEtConfirmNewPassword.setError(getText(R.string.confirmPasswordRequired));
                binding.acpEtConfirmNewPassword.requestFocus();
                return;
            }

            if (!confirmNewPassword.equals(newPassword)){
                binding.acpEtConfirmNewPassword.setError(getText(R.string.bothPasswordDoNotMatch));
                binding.acpEtConfirmNewPassword.requestFocus();
                return;
            }

            organiser.setPassword(newPassword);
            mOrganiserViewModel.updateOrganiser(organiser);
            Toast.makeText(this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
            finish();

        }

        if (userType.equals("user")){
            List<User> mUserList = mUserViewModel.getUserById(userID);
            User user = mUserList.get(0);
            Log.d("TAG", "user changePassword: " + userID);
            String currentPassword = user.getPassword();

            if (oldPassword.isEmpty()){
                binding.acpEtOldPassword.setError(getText(R.string.passwordRequired));
                binding.acpEtOldPassword.requestFocus();
                return;
            }

            if (!oldPassword.equals(currentPassword)){
                binding.acpEtOldPassword.setError(getText(R.string.incorrectPassword));
                binding.acpEtOldPassword.requestFocus();
                return;
            }

            if (newPassword.equals(oldPassword)){
                binding.acpEtNewPassword.setError(getText(R.string.userOtherPassword));
                binding.acpEtNewPassword.requestFocus();
                return;
            }

            if (newPassword.isEmpty()){
                binding.acpEtNewPassword.setError(getText(R.string.passwordRequired));
                binding.acpEtNewPassword.requestFocus();
                return;
            }

            if (newPassword.length()<8){
                binding.acpEtNewPassword.setError(getText(R.string.passwordAtLeast8Char));
                binding.acpEtNewPassword.requestFocus();
                return;
            }

            if (confirmNewPassword.isEmpty()){
                binding.acpEtConfirmNewPassword.setError(getText(R.string.confirmPasswordRequired));
                binding.acpEtConfirmNewPassword.requestFocus();
                return;
            }

            if (!confirmNewPassword.equals(newPassword)){
                binding.acpEtConfirmNewPassword.setError(getText(R.string.bothPasswordDoNotMatch));
                binding.acpEtConfirmNewPassword.requestFocus();
                return;
            }

            user.setPassword(newPassword);
            mUserViewModel.updateUser(user);
            Toast.makeText(this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //close the current activity
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}