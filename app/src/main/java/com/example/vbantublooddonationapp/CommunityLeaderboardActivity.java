package com.example.vbantublooddonationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.AppointmentViewModel;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.ActivityCommunityLeaderboardBinding;
import com.example.vbantublooddonationapp.databinding.ActivityCommunityNewPostBinding;

import java.util.List;
import java.util.Objects;

public class CommunityLeaderboardActivity extends AppCompatActivity {

    private ActivityCommunityLeaderboardBinding mCommunityLeaderboardBinding;

    private AppointmentViewModel mAppointmentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //implementing binding
        mCommunityLeaderboardBinding = ActivityCommunityLeaderboardBinding.inflate(getLayoutInflater());
        View v = mCommunityLeaderboardBinding.getRoot();
        setContentView(v);

        //set toolbar and display icon
        Toolbar toolbar = mCommunityLeaderboardBinding.aclToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        //initialise view model
        mAppointmentViewModel = new ViewModelProvider(this).get(AppointmentViewModel.class);

        //LiveData<List<Appointment>> mAppointmentCompletedList = mAppointmentViewModel.getAllCompletedAppointment();
        //mAppointmentCompleted = mAppointmentCompletedList.get(0);
        //mCommunityLeaderboardBinding.  .setText(mAppointmentCompleted.());


    }

    //back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //close activity
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}