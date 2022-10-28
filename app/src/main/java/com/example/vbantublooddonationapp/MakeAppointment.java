package com.example.vbantublooddonationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.AppointmentViewModel;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.ActivityMakeAppointmentBinding;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class MakeAppointment extends AppCompatActivity {


    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private int mOrganiserID = 1;
    private String mUserType = "user";
    private UserViewModel mUserViewModel;
    private OrganiserViewModel mOrganiserViewModel;
    private AppointmentViewModel mAppointmentViewModel;
    private User mUser;
    private Organiser mOrganiser;

    private DatePickerDialog datePickerDialog;
    private ActivityMakeAppointmentBinding binding;
    private String donationBefore = "";
    private String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMakeAppointmentBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        Toolbar toolbar = binding.amaTbToolBar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        Intent i = getIntent();
        int organiserID = i.getIntExtra("currentOrganiserID", 1);

        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mOrganiserViewModel = new ViewModelProvider(this).get(OrganiserViewModel.class);
        mAppointmentViewModel = new ViewModelProvider(this).get(AppointmentViewModel.class);

        List<Organiser> mOrganiserList = mOrganiserViewModel.getOrganiserById(organiserID);
        mOrganiser = mOrganiserList.get(0);
        mOrganiserID = mOrganiser.getOrganiserID();

        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp", MODE_PRIVATE);

        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)){
            mUserID = mPreferences.getInt(USERID_KEY, 1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        List<User> userList = mUserViewModel.getUserById(mUserID);
        mUser = userList.get(0);

        binding.amaTvBloodDonationCenter.setText(mOrganiser.companyName);
        initUserInfo(mUser);

        //Date session
        initDatePicker();
        binding.amaTvPickDate.setText(R.string.dateFormat);
        
        //button reset clicked
        binding.amaBtnReset.setOnClickListener(view -> resetForm());
        
        //button submit clicked
        binding.amaBtnSubmit.setOnClickListener(view -> submitForm());
    }

    private void initUserInfo(User user) {
        binding.amaTvFullName.setText(user.getFullName());
        binding.amaTvIcNo.setText(user.getIcNo());
        binding.amaTvContactNo.setText(user.getContact());
        binding.amaTvEmail.setText(user.getEmail());
        binding.amaTvPickBlood.setText(user.getBloodType());
        gender = user.getGender();
        if (gender.equals("female")){
            binding.amaRbFemale.setChecked(true);
            binding.amaRbFemale.setClickable(false);
            binding.amaRbMale.setClickable(false);
            binding.amaRgGender.setEnabled(false);
        }

        else if (gender.equals("male")){
            binding.amaRbMale.setChecked(true);
            binding.amaRbFemale.setClickable(false);
            binding.amaRbMale.setClickable(false);
            binding.amaRgGender.setEnabled(false);
        }
        //for fail to retrieve data from database
        else{
            binding.amaRbFemale.setClickable(false);
            binding.amaRbMale.setClickable(false);
            binding.amaRgGender.setEnabled(false);
        }

        binding.amaEtAddress.setText("");
        binding.amaTvPickDate.setText(R.string.dateFormat);
        binding.amaSpPickTime.setSelection(0);


        binding.amaRgDonateHistory.clearCheck();
        donationBefore = "";



    }

    private void resetForm() {
        //reset to user initial data(eg User named Ze Yun came into this page and the initial value at full name should be Ze Yun, and following value based on Ze Yun's profile)
        initUserInfo(mUser);
    }

    private void submitForm() {
        String fullName = binding.amaTvFullName.getText().toString().trim();
        String icNo = binding.amaTvIcNo.getText().toString().trim();
        String contactNo = binding.amaTvContactNo.getText().toString().trim();
        String email = binding.amaTvEmail.getText().toString().trim();
        String address = binding.amaEtAddress.getText().toString().trim();
        String appointmentDate = binding.amaTvPickDate.getText().toString().trim();
        String appointmentTime = binding.amaSpPickTime.getSelectedItem().toString().trim();
        String pickBlood = binding.amaTvPickBlood.getText().toString().trim();


        //validation
        //address validation
        if (address.isEmpty()){
            binding.amaEtAddress.setError("Address is required!");
            binding.amaEtAddress.requestFocus();
            return;
        }

        //pickDate validation
        if (appointmentDate.equals("DD/MM/YYYY")){
            Toast.makeText(this, "Please select your appointment date", Toast.LENGTH_SHORT).show();
            binding.amaTvPickDate.requestFocus();
            return;
        }

        //pickTime validation
        if (appointmentTime.equals("Select time")){
            Toast.makeText(this, "Please select your appointment time", Toast.LENGTH_SHORT).show();
            binding.amaSpPickTime.requestFocus();
            return;
        }

        //donationBefore validation
        if (donationBefore.isEmpty()){
            Toast.makeText(this, "Please select your donation history experience", Toast.LENGTH_SHORT).show();
            binding.amaRgDonateHistory.requestFocus();
            return;
        }

        //if all success
        //assign value and insert to database
        Appointment appointment = new Appointment(mUserID, mOrganiserID, appointmentTime, appointmentDate, donationBefore, "0ml", 0);
        mAppointmentViewModel.insertAppointment(appointment);
        Toast.makeText(this, "Appointment submitted successfully", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(MakeAppointment.this,AppointmentSuccess.class);
        startActivity(i);
        finish();

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

    public void rgDonateHistory_clicked(View view) {
        int x = binding.amaRgDonateHistory.getCheckedRadioButtonId();
        RadioButton r = findViewById(x);
        donationBefore = r.getText().toString().trim();
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