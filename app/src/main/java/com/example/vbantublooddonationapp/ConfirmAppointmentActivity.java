
package com.example.vbantublooddonationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.AppointmentViewModel;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.ActivityConfirmAppointmentBinding;
import com.example.vbantublooddonationapp.databinding.ActivityOrganiserSingleAppointmentDetailsBinding;

import java.util.List;
import java.util.Objects;

public class ConfirmAppointmentActivity extends AppCompatActivity {

    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private String mUserType = "user";

    private ActivityConfirmAppointmentBinding binding;
    private OrganiserViewModel mOrganiserViewModel;
    private AppointmentViewModel mAppointmentViewModel;
    private UserViewModel mUserViewModel;
    private Appointment mAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmAppointmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mOrganiserViewModel = new ViewModelProvider(this).get(OrganiserViewModel.class);
        mAppointmentViewModel = new ViewModelProvider(this).get(AppointmentViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        Toolbar toolbar = binding.acaToolbar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp",MODE_PRIVATE);

        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)) {
            mUserID = mPreferences.getInt(USERID_KEY,1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        Intent i = getIntent();
        int appointmentID = i.getIntExtra("currentAppointmentID", 1);
        int appointmentUserID = i.getIntExtra("appointmentUserID", 1);

        List<Organiser> organiserList = mOrganiserViewModel.getOrganiserById(mUserID);
        Organiser mOrganiser = organiserList.get(0);

        List<User> userList = mUserViewModel.getUserById(appointmentUserID);
        User mUser = userList.get(0);

        List<Appointment> appointmentList = mAppointmentViewModel.getAppointmentById(appointmentID);
        mAppointment = appointmentList.get(0);

        binding.acaTvLocation.setText(mOrganiser.getCompanyName());
        binding.acaTvDonorName.setText(mUser.getFullName());
        binding.acaTvDate.setText(getFullDate(mAppointment.getAppointmentDate()));
        binding.acaTvTime.setText(mAppointment.getAppointmentTime());

        binding.acaBtnConfirmAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmAppointment();
            }
        });
    }

    private void confirmAppointment() {
        String bloodAmtStr = binding.acaSpinnerBloodAmt.getSelectedItem().toString().trim();

        //validate to check if date of birth is empty
        if (bloodAmtStr.equals("Select amount")) {
            Toast.makeText(this, R.string.bloodAmountRequiredToast, Toast.LENGTH_SHORT).show();
            binding.acaSpinnerBloodAmt.requestFocus();
            return;
        }

        int bloodAmt = getBloodAmt(bloodAmtStr);
        mAppointment.setBloodAmt(bloodAmt);
        mAppointment.setStatus("Completed");
        mAppointmentViewModel.updateAppointment(mAppointment);

        Toast.makeText(this, R.string.userAppointmentConfirmedToast, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ConfirmAppointmentActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public int getBloodAmt(String bloodMl) {
        switch (bloodMl) {
            case "200 ml":
                return 200;
            case "250 ml":
                return 250;
            case "300 ml":
                return 300;
            case "350 ml":
                return 350;
            case "400 ml":
                return 400;
            case "450 ml":
                return 450;
            case "500 ml":
                return 500;
        }
        return 0;
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