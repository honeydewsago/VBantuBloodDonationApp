package com.example.vbantublooddonationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.Model.BloodRequest;
import com.example.vbantublooddonationapp.ViewModel.AppointmentViewModel;
import com.example.vbantublooddonationapp.ViewModel.BloodRequestViewModel;
import com.example.vbantublooddonationapp.adapter.LocationAdapter;
import com.example.vbantublooddonationapp.adapter.UrgentRequestAdapter;
import com.example.vbantublooddonationapp.adapter.UserAppointmentAdapter;
import com.example.vbantublooddonationapp.databinding.ActivityBloodRequestHistoryBinding;
import com.example.vbantublooddonationapp.databinding.ActivityUserAppointmentsBinding;

import java.util.List;
import java.util.Objects;

public class UserAppointmentsActivity extends AppCompatActivity {

    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private String mUserType = "user";

    private ActivityUserAppointmentsBinding binding;
    private AppointmentViewModel mAppointmentViewModel;
    private UserAppointmentAdapter mUserAppointmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserAppointmentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.auaToolbar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp",MODE_PRIVATE);

        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)) {
            mUserID = mPreferences.getInt(USERID_KEY,1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        mUserAppointmentAdapter = new UserAppointmentAdapter(this);
        binding.auaRvAppointments.setAdapter(mUserAppointmentAdapter);

        //set the layout manager
        binding.auaRvAppointments.setLayoutManager(new GridLayoutManager(this,getResources().getInteger(R.integer.grid_column_count)));

        initAppointmentViewModel();
    }

    private void initAppointmentViewModel() {
        //initialize the view model from the BloodRequestViewModel
        mAppointmentViewModel = new ViewModelProvider(this).get(AppointmentViewModel.class);

        //initialize the observer to observe the Live Data
        final Observer<List<Appointment>> appointmentListObserver = new Observer<List<Appointment>>() {
            @Override
            public void onChanged(List<Appointment> appointments) {
                mUserAppointmentAdapter.setAppointmentList(appointments);
            }
        };

        mAppointmentViewModel.getAppointmentByOrganiserID(mUserID).observe(this,appointmentListObserver);
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