package com.example.vbantublooddonationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.databinding.ActivityRegisterUserBinding;

import java.util.Calendar;

public class RegisterUserActivity extends AppCompatActivity {

    private ActivityRegisterUserBinding binding;
    private String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterUserBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

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
        String username = binding.aruEtUsername.getText().toString();
        String icNo = binding.aruEtIcNo.getText().toString();
        String contactNo = binding.aruEtContactNo.getText().toString();
        String password = binding.aruEtPassword.getText().toString();
        String confirmPassword = binding.aruEtConfirmPassword.getText().toString();

        //validate to check if date of birth is empty
        if (dateOfBirth.equals("Click To Select")) {
            Toast.makeText(this, "Date of birth is required!", Toast.LENGTH_SHORT).show();
            binding.aruTvSelectDateOfBirth.requestFocus();
            return;
        }
        //validate to check if gender is empty
        if (gender.isEmpty()) {
            Toast.makeText(this, "Gender is required!", Toast.LENGTH_SHORT).show();
            binding.aruRgGender.requestFocus();
            return;
        }
        //validate to check if username is empty
        if (username.isEmpty()) {
            binding.aruEtUsername.setError("Username is required!");
            binding.aruEtUsername.requestFocus();
            return;
        }
        //validate to check if ic number is empty
        if (icNo.isEmpty()) {
            binding.aruEtIcNo.setError("IC number is required!");
            binding.aruEtIcNo.requestFocus();
            return;
        }
        //validate to check if ic number is equal to 12 characters
        if (icNo.length() != 12) {
            binding.aruEtIcNo.setError("IC number must be 12 characters!");
            binding.aruEtIcNo.requestFocus();
            return;
        }
        //validate to check if contact number is empty
        if (contactNo.isEmpty()) {
            binding.aruEtContactNo.setError("Contact number is required!");
            binding.aruEtContactNo.requestFocus();
            return;
        }
        //validate to check if contact number is less than 10 characters
        if (contactNo.length() < 10) {
            binding.aruEtContactNo.setError("Contact number must be at least 10 characters!");
            binding.aruEtContactNo.requestFocus();
            return;
        }
        //validate to check if password is empty
        if (password.isEmpty()) {
            binding.aruEtPassword.setError("Password is required!");
            binding.aruEtPassword.requestFocus();
            return;
        }
        //validate to check if password is less than 8 characters
        if (password.length() < 8) {
            binding.aruEtPassword.setError("Password should contain at least 8 characters!");
            binding.aruEtPassword.requestFocus();
            return;
        }
        //validate to check if confirm password is empty
        if (confirmPassword.isEmpty()) {
            binding.aruEtConfirmPassword.setError("Confirm password is required!");
            binding.aruEtConfirmPassword.requestFocus();
            return;
        }
        //validate to check if both password match
        if (!confirmPassword.equals(password)) {
            binding.aruEtConfirmPassword.setError("Both passwords does not match!");
            binding.aruEtConfirmPassword.requestFocus();
            return;
        }

        User user = new User(username,password,fullName,email,icNo,dateOfBirth,gender,contactNo,"A+");
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