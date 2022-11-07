package com.example.vbantublooddonationapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.ViewModel.AppointmentViewModel;
import com.example.vbantublooddonationapp.adapter.AppointmentHistoryAdapter;
import com.example.vbantublooddonationapp.databinding.ActivityAppointmentHistoryBinding;

import java.util.List;
import java.util.Objects;

public class AppointmentHistoryActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private String mUserType = "user";
    private ActivityAppointmentHistoryBinding binding;
    private AppointmentHistoryAdapter mAppointmentAdapter;
    private AppointmentViewModel mAppointmentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppointmentHistoryBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        toolbar = binding.ahToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp",MODE_PRIVATE);

        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)) {
            mUserID = mPreferences.getInt(USERID_KEY,1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        mAppointmentAdapter =  new AppointmentHistoryAdapter(this);
        binding.aahRvAppointmentHistory.setAdapter(mAppointmentAdapter);

        binding.aahRvAppointmentHistory.setLayoutManager(new LinearLayoutManager(this));

        initAppointmentViewModel();
    }

    private void initAppointmentViewModel() {
        //initialize the view model from the BloodRequestViewModel
        mAppointmentViewModel = new ViewModelProvider(this).get(AppointmentViewModel.class);

        //initialize the observer to observe the Live Data
        final Observer<List<Appointment>> appointmentListObserver = new Observer<List<Appointment>>() {
            @Override
            public void onChanged(List<Appointment> appointments) {
                mAppointmentAdapter.setAppointmentList(appointments);
            }
        };

        mAppointmentViewModel.getRequestByUserId(mUserID).observe(this,appointmentListObserver);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}