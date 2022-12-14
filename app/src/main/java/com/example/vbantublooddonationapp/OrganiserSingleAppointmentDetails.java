package com.example.vbantublooddonationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.AppointmentViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.ActivityOrganiserSingleAppointmentDetailsBinding;

import java.util.List;
import java.util.Objects;

public class OrganiserSingleAppointmentDetails extends AppCompatActivity {

    private ActivityOrganiserSingleAppointmentDetailsBinding binding;
    private AppointmentViewModel mAppointmentViewModel;
    private UserViewModel mUserViewModel;
    private Appointment mAppointment;
    private User mUser;
    String bloodAmt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrganiserSingleAppointmentDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAppointmentViewModel = new ViewModelProvider(this).get(AppointmentViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        Toolbar toolbar = binding.aosadToolbar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        Intent i = getIntent();
        int appointmentID = i.getIntExtra("currentAppointmentID", 1);

        List<Appointment> appointmentList = mAppointmentViewModel.getAppointmentById(appointmentID);
        mAppointment = appointmentList.get(0);

        List<User> userList = mUserViewModel.getUserById(mAppointment.getUserID());
        mUser = userList.get(0);

        bloodAmt = String.valueOf(mAppointment.getBloodAmt());
        binding.aosadTvFullName.setText(mUser.getFullName());
        binding.aosadTvIcNo.setText(mUser.getIcNo());
        binding.aosadTvContactNo.setText(mUser.getContact());
        binding.aosadTvEmail.setText(mUser.getEmail());
        binding.aosadTvAddress.setText(mAppointment.getAddress());
        binding.aosadTvDate.setText(getFullDate(mAppointment.getAppointmentDate()));
        binding.aosadTvTime.setText(mAppointment.getAppointmentTime());
        binding.aosadTvBloodGroup.setText(mUser.getBloodType());
        binding.aosadTvDonationAmount.setText(bloodAmt);

        if (mUser.getGender().equals("female")) {
            binding.aosadRbFemale.setChecked(true);
        }
        else {
            binding.aosadRbMale.setChecked(true);
        }

        if (mAppointment.getDonationBefore().equals("Yes")) {
            binding.aosadRbYes.setChecked(true);
        }
        else {
            binding.aosadRbNo.setChecked(true);
        }

        if (mAppointment.getStatus().equals("Completed")) {
            binding.aosadBtnScanQR.setEnabled(false);
        }

        binding.aosadBtnScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scanCode = new Intent(OrganiserSingleAppointmentDetails.this, ScanQRCode.class);
                scanCode.putExtra("currentAppointmentID", mAppointment.getAppointmentID());
                scanCode.putExtra("appointmentUserID", mUser.getUserID());
                startActivity(scanCode);
            }
        });
    }

    public String getFullDate(String dateTime) {
        String year = dateTime.substring(0,4);
        int month = Integer.parseInt(dateTime.substring(4,6));
        String day = dateTime.substring(6,8);

        return day + " "+ getMonthName(month) + " " + year;
    }

    public String getMonthName(int month_value){
        switch (month_value) {
            case 1:
                return getResources().getString(R.string.january);
            case 2:
                return getResources().getString(R.string.february);
            case 3:
                return getResources().getString(R.string.march);
            case 4:
                return getResources().getString(R.string.april);
            case 5:
                return getResources().getString(R.string.may);
            case 6:
                return getResources().getString(R.string.june);
            case 7:
                return getResources().getString(R.string.july);
            case 8:
                return getResources().getString(R.string.august);
            case 9:
                return getResources().getString(R.string.september);
            case 10:
                return getResources().getString(R.string.october);
            case 11:
                return getResources().getString(R.string.november);
            case 12:
                return getResources().getString(R.string.december);
            default:
                return getResources().getString(R.string.month);
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