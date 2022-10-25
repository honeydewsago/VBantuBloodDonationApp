package com.example.vbantublooddonationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.vbantublooddonationapp.databinding.ActivityMakeAppointmentBinding;

import java.util.Calendar;

public class MakeAppointment extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private ActivityMakeAppointmentBinding binding;
    private String gender = "";
    private String donationBefore = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMakeAppointmentBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        binding.amaBtnBack.setOnClickListener(view -> finish());
        
        //Date session
        initDatePicker();
        binding.amaTvPickDate.setText(R.string.dateFormat);
        
        //button reset clicked
        binding.amaBtnReset.setOnClickListener(view -> resetForm());
        
        //button submit clicked
        binding.amaBtnSubmit.setOnClickListener(view -> submitForm());
    }

    private void resetForm() {
        //reset to user initial data(eg User named Ze Yun came into this page and the initial value at full name should be Ze Yun, and following value based on Ze Yun's profile)
    }

    private void submitForm() {
        String fullName = binding.amaEtFullName.getText().toString().trim();
        String icNo = binding.amaEtIcNo.getText().toString().trim();
        String contactNo = binding.amaEtContactNo.getText().toString().trim();
        String email = binding.amaEtEmail.getText().toString().trim();
        String address = binding.amaEtAddress.getText().toString().trim();
        String appointmentDate = binding.amaTvPickDate.getText().toString().trim();
        String appointmentTime = binding.amaSpPickTime.getSelectedItem().toString().trim();
        String pickBlood = binding.amaSpPickBlood.getSelectedItem().toString().trim();


        //validation
        //full name validation
        if (fullName.isEmpty()){
            binding.amaEtFullName.setError("Full Name is Required!");
            binding.amaEtFullName.requestFocus();
            return;
        }

        //ic no validation
        if (icNo.isEmpty()){
            binding.amaEtIcNo.setError("IC No is Required!");
            binding.amaEtIcNo.requestFocus();
            return;
        }

        //ic no length validation
        if (icNo.length() != 12){
            binding.amaEtIcNo.setError("IC No length should be 12!");
            binding.amaEtIcNo.requestFocus();
            return;
        }

        //contact no validation
        if (contactNo.isEmpty()){
            binding.amaEtContactNo.setError("Contact No is Required!");
            binding.amaEtContactNo.requestFocus();
            return;
        }

        //contact no length validation
        if (contactNo.length()< 10){
            binding.amaEtContactNo.setError("Contact No is Invalid!");
            binding.amaEtContactNo.requestFocus();
            return;
        }

        //email validation
        if (email.isEmpty()){
            binding.amaEtEmail.setError("Email Address is Required");
            binding.amaEtEmail.requestFocus();
            return;
        }

        //email format validation
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.amaEtEmail.setError("Email address is Invalid!");
            binding.amaEtEmail.requestFocus();
            return;
        }


        //address validation
        if (address.isEmpty()){
            binding.amaEtAddress.setError("Address is required!");
            binding.amaEtAddress.requestFocus();
            return;
        }

        if (gender.isEmpty()){
            Toast.makeText(this, "Please select your Gender, Gender = " + gender, Toast.LENGTH_SHORT).show();
            binding.amaRgGender.requestFocus();
            return;
        }

        //pickDate validation
        if (appointmentDate.equals("DD/MM/YYYY")){
            Toast.makeText(this, "Please select your appointment date", Toast.LENGTH_SHORT).show();
            binding.amaTvPickDate.requestFocus();
            return;
        }

        if (donationBefore.isEmpty()){
            Toast.makeText(this, "Please select your donation history experience", Toast.LENGTH_SHORT).show();
            binding.amaRgDonateHistory.requestFocus();
            return;
        }

        //if all success
        //assign value and insert to database
    }


    //pick date session
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            binding.amaTvPickDate.setText(date);
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);

        //no go back to past and make appointment
        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year) {
        return day + "/" + month + "/" + year;
    }


    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    public void rgGender_clicked(View view) {
        int x = binding.amaRgGender.getCheckedRadioButtonId();
        RadioButton r = findViewById(x);
        gender = r.getText().toString().trim();
    }

    public void rgDonateHistory_clicked(View view) {
        int x = binding.amaRgDonateHistory.getCheckedRadioButtonId();
        RadioButton r = findViewById(x);
        donationBefore = r.getText().toString().trim();
    }
    
}