package com.example.vbantublooddonationapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.ActivityRegisterUserBinding;

import java.util.Calendar;
import java.util.List;

public class RegisterUserActivity extends AppCompatActivity {

    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private String mUserType = "";
    private ActivityRegisterUserBinding binding;
    private UserViewModel mUserViewModel;
    private String gender="";

    private boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp",MODE_PRIVATE);

        Intent i = getIntent();
        String email = i.getStringExtra("user_email");
        String fullName = i.getStringExtra("user_fullName");

        binding.aruBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser(email,fullName);
            }
        });
    }

    private void registerUser(String email, String fullName){
        String dateOfBirth = binding.aruTvSelectDateOfBirth.getText().toString().trim();
        String bloodType = binding.aruSpinnerBloodType.getSelectedItem().toString().trim();
        String username = binding.aruEtUsername.getText().toString().trim();
        String icNo = binding.aruEtIcNo.getText().toString().trim();
        String contactNo = binding.aruEtContactNo.getText().toString().trim();
        String password = binding.aruEtPassword.getText().toString().trim();
        String confirmPassword = binding.aruEtConfirmPassword.getText().toString().trim();

        //validate to check if date of birth is empty
        if (dateOfBirth.equals("Click To Select")) {
            Toast.makeText(this, R.string.dateOfBirthRequiredToast, Toast.LENGTH_SHORT).show();
            binding.aruTvSelectDateOfBirth.requestFocus();
            return;
        }

        //validate to check if blood type is empty
        if (bloodType.equals("Select blood type")) {
            Toast.makeText(this, R.string.bloodTypeRequiredToast, Toast.LENGTH_SHORT).show();
            binding.aruSpinnerBloodType.requestFocus();
            return;
        }

        //validate to check if gender is empty
        if (gender.isEmpty()) {
            Toast.makeText(this, R.string.genderRequiredToast, Toast.LENGTH_SHORT).show();
            binding.aruRgGender.requestFocus();
            return;
        }
        //validate to check if username is empty
        if (username.isEmpty()) {
            binding.aruEtUsername.setError(getText(R.string.usernameRequired));
            binding.aruEtUsername.requestFocus();
            return;
        }
        //validate to check if ic number is empty
        if (icNo.isEmpty()) {
            binding.aruEtIcNo.setError(getText(R.string.icNumberRequired));
            binding.aruEtIcNo.requestFocus();
            return;
        }
        //validate to check if ic number is equal to 12 characters
        if (icNo.length() != 12) {
            binding.aruEtIcNo.setError(getText(R.string.icNumAtLeast12Char));
            binding.aruEtIcNo.requestFocus();
            return;
        }
        //validate to check if contact number is empty
        if (contactNo.isEmpty()) {
            binding.aruEtContactNo.setError(getText(R.string.contactNumberRequired));
            binding.aruEtContactNo.requestFocus();
            return;
        }
        //validate to check if contact number is less than 10 characters
        if (contactNo.length() < 10) {
            binding.aruEtContactNo.setError(getText(R.string.contactNumAtLeast10Char));
            binding.aruEtContactNo.requestFocus();
            return;
        }
        //validate to check if password is empty
        if (password.isEmpty()) {
            binding.aruEtPassword.setError(getText(R.string.passwordRequired));
            binding.aruEtPassword.requestFocus();
            return;
        }
        //validate to check if password is less than 8 characters
        if (password.length() < 8) {
            binding.aruEtPassword.setError(getText(R.string.passwordAtLeast8Char));
            binding.aruEtPassword.requestFocus();
            return;
        }
        //validate to check if confirm password is empty
        if (confirmPassword.isEmpty()) {
            binding.aruEtConfirmPassword.setError(getText(R.string.confirmPasswordRequired));
            binding.aruEtConfirmPassword.requestFocus();
            return;
        }
        //validate to check if both password match
        if (!confirmPassword.equals(password)) {
            binding.aruEtConfirmPassword.setError(getText(R.string.bothPasswordDoNotMatch));
            binding.aruEtConfirmPassword.requestFocus();
            return;
        }

        User user = new User(username,password,fullName,email,icNo,dateOfBirth,gender,contactNo,bloodType);

        //insert the user object
        mUserViewModel.insertUser(user);

        List<User> mUserList = mUserViewModel.loginUser(email,password);
        User userReg = mUserList.get(0);
        mUserID = userReg.userID;
        mUserType = "user";
        status = true;

        if (status) {
            SharedPreferences.Editor spEditor = mPreferences.edit();
            spEditor.putInt(USERID_KEY, mUserID);
            spEditor.putString(USERTYPE_KEY, mUserType);
            spEditor.apply();
        }

        //toast message to inform users the registration is successful
        Toast.makeText(RegisterUserActivity.this, R.string.userRegisterSuccessfully, Toast.LENGTH_SHORT).show();

        startActivity(new Intent(RegisterUserActivity.this, HomeActivity.class));
    }

    public void tvSelectDateOfBirth_clicked(View view) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //create date picker dialog for user to select date
        DatePickerDialog birthDate = new DatePickerDialog(
                this,
                (view1, year1, month1, dayOfMonth) -> {
                    //set date
                    binding.aruTvSelectDateOfBirth.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
                },
                year,
                month,
                day
        );

        birthDate.getDatePicker().setMaxDate(System.currentTimeMillis());

        //show date picker dialog
        birthDate.show();
    }

    public void rgGender_clicked(View view) {
        int x = binding.aruRgGender.getCheckedRadioButtonId();
        RadioButton r = findViewById(x);
        gender = r.getText().toString().trim();
    }
}