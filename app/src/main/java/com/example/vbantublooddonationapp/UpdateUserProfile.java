package com.example.vbantublooddonationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.ActivityUpdateUserProfileBinding;

import java.util.List;
import java.util.Objects;

public class UpdateUserProfile extends AppCompatActivity {

    private ActivityUpdateUserProfileBinding binding;
    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private User mUser;
    private UserViewModel mUserViewModel;
    private String mUserType = "user";
    private int mUserID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateUserProfileBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        //setup toolbar
        Toolbar toolbar = binding.auupTbToolBar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp", MODE_PRIVATE);
        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)){
            mUserID = mPreferences.getInt(USERID_KEY, 1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        List<User> userList = mUserViewModel.getUserById(mUserID);
        mUser = userList.get(0);

        initProfile(mUser);

        binding.auupBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

    }

    private void updateProfile() {
        String userName = binding.auupEtUsername.getText().toString();
        String contact = binding.auupEtContact.getText().toString();
        String email = binding.auupEtEmail.getText().toString();

        if (userName.isEmpty()){
            binding.auupEtUsername.setError(getText(R.string.usernameRequired));
            binding.auupEtUsername.requestFocus();
            return;
        }

        if (contact.isEmpty()){
            binding.auupEtContact.setError(getText(R.string.contactNumberRequired));
            binding.auupEtContact.requestFocus();
            return;
        }

        if (contact.length()<10){
            binding.auupEtContact.setError(getText(R.string.contactNumAtLeast10Char));
            binding.auupEtContact.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.auupEtEmail.setError(getText(R.string.emailInvalid));
            binding.auupEtEmail.requestFocus();
            return;
        }

        if (email.isEmpty()){
            binding.auupEtEmail.setError(getText(R.string.emailRequired));
            binding.auupEtEmail.requestFocus();
            return;
        }

        //update room database
        mUser.setUsername(userName);
        mUser.setContact(contact);
        mUser.setEmail(email);
        mUserViewModel.updateUser(mUser);

        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void initProfile(User user) {
        binding.auupTvFullNameDisplay.setText(user.getFullName());
        binding.auupTvBloodTypeDisplay.setText(user.getBloodType());
        binding.auupEtUsername.setText(user.getUsername());
        binding.auupEtContact.setText(user.getContact());
        binding.auupEtEmail.setText(user.getEmail());
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