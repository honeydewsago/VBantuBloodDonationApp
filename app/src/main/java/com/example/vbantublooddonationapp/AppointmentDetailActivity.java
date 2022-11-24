package com.example.vbantublooddonationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.AppointmentViewModel;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.ActivityAppointmentDetailBinding;

import java.util.List;
import java.util.Objects;

public class AppointmentDetailActivity extends AppCompatActivity {
    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private int mAppointmentID = 1;
    private int mOrganiserID = 1;
    private String mUserType = "user";

    private UserViewModel mUserViewModel;
    private OrganiserViewModel mOrganiserViewModel;
    private AppointmentViewModel mAppointmentViewModel;
    private ActivityAppointmentDetailBinding binding;
    private Appointment mAppointment;
    private User mUser;
    private Organiser mOrganiser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding with the layout xml file
        binding = ActivityAppointmentDetailBinding.inflate(getLayoutInflater());

        View v = binding.getRoot();
        //set content view
        setContentView(v);
        //binding toolbar
        Toolbar toolbar = binding.aadToolbar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        //getting the intent
        Intent i = getIntent();
        //getting value stored in intent
        int appointmentID = i.getIntExtra("currentAppointmentID", 1);
        //initialize view model
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mOrganiserViewModel = new ViewModelProvider(this).get(OrganiserViewModel.class);
        mAppointmentViewModel = new ViewModelProvider(this).get(AppointmentViewModel.class);

        //get appointment list with  appointment id
        List<Appointment> mAppointmentList = mAppointmentViewModel.getAppointmentById(appointmentID);
        mAppointment = mAppointmentList.get(0);
        mAppointmentID = mAppointment.getOrganiserID();
        //get shared pref
        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp", MODE_PRIVATE);
        //get values from shared pref
        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)){
            mUserID = mPreferences.getInt(USERID_KEY, 1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }
        //get user list with user id
        List<User> userList = mUserViewModel.getUserById(mUserID);
        mUser = userList.get(0);

        mOrganiserID = mAppointment.getOrganiserID();
        List<Organiser> mOrganiserList = mOrganiserViewModel.getOrganiserById(mOrganiserID);
        mOrganiser = mOrganiserList.get(0);

        //initialize appointment info
        initAppointmentInfo(mUser, mAppointment, mOrganiser);
    }


    private void initAppointmentInfo(User user, Appointment appointment, Organiser organiser) {
        //setting all the details of the appointment
        binding.aadTvFullName.setText(user.getFullName());
        binding.aadTvIcNo.setText(user.getIcNo());
        binding.aadTvContactNo.setText(user.getContact());
        binding.aadTvEmail.setText(user.getEmail());
        binding.aadTvAddress.setText(appointment.getAddress());
        String gender = user.getGender();
        //set all radio button to be non clickable
        if (gender.equals("female")){
            binding.aadRbFemale.setChecked(true);
            binding.aadRbFemale.setClickable(false);
            binding.aadRbMale.setClickable(false);
            binding.aadRgGender.setEnabled(false);
        }

        else if (gender.equals("male")){
            binding.aadRbMale.setChecked(true);
            binding.aadRbFemale.setClickable(false);
            binding.aadRbMale.setClickable(false);
            binding.aadRgGender.setEnabled(false);
        }
        //for fail to retrieve data from database
        else{
            binding.aadRbFemale.setClickable(false);
            binding.aadRbMale.setClickable(false);
            binding.aadRgGender.setEnabled(false);
        }

        binding.aadTvBloodDonationCenter.setText(organiser.getCompanyName());
        binding.aadTvPickDate.setText(getFullDate(appointment.getAppointmentDate()));
        binding.aadTvAppointmentTime.setText(appointment.getAppointmentTime());
        binding.aadTvPickBlood.setText(user.getBloodType());

        String donateHistory = appointment.getDonationBefore();

        if(donateHistory.equals("No")){
            binding.aadRbDonateHistoryNo.setChecked(true);
            binding.aadRbDonateHistoryNo.setClickable(false);
            binding.aadRbDonateHistoryYes.setClickable(false);
            binding.aadRgDonateHistory.setEnabled(false);
        }else if (donateHistory.equals("Yes")){
            binding.aadRbDonateHistoryYes.setChecked(true);
            binding.aadRbDonateHistoryYes.setClickable(false);
            binding.aadRbDonateHistoryNo.setClickable(false);
            binding.aadRgDonateHistory.setEnabled(false);
        }
    }

    //function that convert date into a readable format
    public String getFullDate(String dateTime) {
        String year = dateTime.substring(0,4);
        int month = Integer.parseInt(dateTime.substring(4,6));
        String day = dateTime.substring(6,8);

        return day + " "+ getMonthName(month) + " " + year;
    }

    public String getMonthName(int month_value){
        switch (month_value) {
            case 1:
                return binding.getRoot().getResources().getString(R.string.january);
            case 2:
                return binding.getRoot().getResources().getString(R.string.february);
            case 3:
                return binding.getRoot().getResources().getString(R.string.march);
            case 4:
                return binding.getRoot().getResources().getString(R.string.april);
            case 5:
                return binding.getRoot().getResources().getString(R.string.may);
            case 6:
                return binding.getRoot().getResources().getString(R.string.june);
            case 7:
                return binding.getRoot().getResources().getString(R.string.july);
            case 8:
                return binding.getRoot().getResources().getString(R.string.august);
            case 9:
                return binding.getRoot().getResources().getString(R.string.september);
            case 10:
                return binding.getRoot().getResources().getString(R.string.october);
            case 11:
                return binding.getRoot().getResources().getString(R.string.november);
            case 12:
                return binding.getRoot().getResources().getString(R.string.december);
            default:
                return binding.getRoot().getResources().getString(R.string.month);
        }
    }

    //back function
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}