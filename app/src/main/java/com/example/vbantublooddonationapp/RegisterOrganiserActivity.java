package com.example.vbantublooddonationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.databinding.ActivityRegisterOrganiserBinding;

import java.util.List;

public class RegisterOrganiserActivity extends AppCompatActivity {
    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private int mUserID = 1;
    private String mUserType = "";
    private SharedPreferences mPreferences;
    private ActivityRegisterOrganiserBinding binding;
    private OrganiserViewModel mOrganiserViewModel;

    private boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_organiser);

        binding = ActivityRegisterOrganiserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mOrganiserViewModel = new ViewModelProvider(this).get(OrganiserViewModel.class);

        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp",MODE_PRIVATE);

        Intent i = getIntent();
        String email = i.getStringExtra("organiser_email");
        String companyName = i.getStringExtra("company_name");

        binding.aroBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerOrganiser(email,companyName);
            }
        });
    }

    private void registerOrganiser(String email, String companyName) {
        String picName = binding.aroEtPicName.getText().toString().trim();
        String contactNo = binding.aroEtContactNo.getText().toString().trim();
        String icNo = binding.aroEtIcNo.getText().toString().trim();
        String address = binding.aroEtAddress.getText().toString().trim();
        String password = binding.aroEtPassword.getText().toString().trim();
        String confirmPassword = binding.aroEtConfirmPassword.getText().toString().trim();

        //validate to check if person-in-charge name is empty
        if (picName.isEmpty()) {
            binding.aroEtPicName.setError(getText(R.string.nameOfPicRequired));
            binding.aroEtPicName.requestFocus();
            return;
        }
        //validate to check if contact number is empty
        if (contactNo.isEmpty()) {
            binding.aroEtContactNo.setError(getText(R.string.contactNumberRequired));
            binding.aroEtContactNo.requestFocus();
            return;
        }
        //validate to check if contact number is less than 10 characters
        if (contactNo.length() < 10) {
            binding.aroEtContactNo.setError(getText(R.string.contactNumAtLeast10Char));
            binding.aroEtContactNo.requestFocus();
            return;
        }
        //validate to check if ic number is empty
        if (icNo.isEmpty()) {
            binding.aroEtIcNo.setError(getText(R.string.icNumberRequired));
            binding.aroEtIcNo.requestFocus();
            return;
        }
        //validate to check if ic number is equal to 12 characters
        if (icNo.length() != 12) {
            binding.aroEtIcNo.setError(getText(R.string.icNumAtLeast12Char));
            binding.aroEtIcNo.requestFocus();
            return;
        }
        //validate to check if address is empty
        if (address.isEmpty()) {
            binding.aroEtAddress.setError(getText(R.string.orgnAddressRequired));
            binding.aroEtAddress.requestFocus();
            return;
        }
        //validate to check if password is empty
        if (password.isEmpty()) {
            binding.aroEtPassword.setError(getText(R.string.passwordRequired));
            binding.aroEtPassword.requestFocus();
            return;
        }
        //validate to check if password is less than 8 characters
        if (password.length() < 8) {
            binding.aroEtPassword.setError(getText(R.string.passwordAtLeast8Char));
            binding.aroEtPassword.requestFocus();
            return;
        }
        //validate to check if confirm password is empty
        if (confirmPassword.isEmpty()) {
            binding.aroEtConfirmPassword.setError(getText(R.string.confirmPasswordRequired));
            binding.aroEtConfirmPassword.requestFocus();
            return;
        }
        //validate to check if both password match
        if (!confirmPassword.equals(password)) {
            binding.aroEtConfirmPassword.setError(getText(R.string.bothPasswordDoNotMatch));
            binding.aroEtConfirmPassword.requestFocus();
            return;
        }

        Organiser organiser = new Organiser(email,companyName,password,picName,contactNo,icNo,address);

        //insert the organiser object
        mOrganiserViewModel.insertOrganiser(organiser);

        List<Organiser> mOrganiserList = mOrganiserViewModel.loginOrganiser(email,password);
        Organiser organiserReg = mOrganiserList.get(0);
        mUserID = organiserReg.organiserID;
        mUserType = "organiser";
        status = true;

        if (status) {
            SharedPreferences.Editor spEditor = mPreferences.edit();
            spEditor.putInt(USERID_KEY, mUserID);
            spEditor.putString(USERTYPE_KEY, mUserType);
            spEditor.apply();
        }

        //toast message to inform organisers the registration is successful
        Toast.makeText(RegisterOrganiserActivity.this, R.string.userRegisterSuccessfully, Toast.LENGTH_SHORT).show();

        startActivity(new Intent(RegisterOrganiserActivity.this, HomeActivity.class));

    }
}