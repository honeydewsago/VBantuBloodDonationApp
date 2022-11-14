package com.example.vbantublooddonationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.Model.LeaderboardUser;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.AppointmentViewModel;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.adapter.LeaderboardAdapter;
import com.example.vbantublooddonationapp.databinding.ActivityCommunityLeaderboardBinding;
import com.example.vbantublooddonationapp.databinding.ActivityCommunityNewPostBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class CommunityLeaderboardActivity extends AppCompatActivity {

    private ActivityCommunityLeaderboardBinding mCommunityLeaderboardBinding;

    private AppointmentViewModel mAppointmentViewModel;
    private UserViewModel mUserViewModel;
    private List<LeaderboardUser> mLeaderboardUserList;
    private LeaderboardAdapter mLeaderboardAdapter;

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

        mAppointmentViewModel = new ViewModelProvider(this).get(AppointmentViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        mLeaderboardUserList = new ArrayList<>();
        List<Appointment> appointmentList = mAppointmentViewModel.getAllCompletedAppointment();

        List<User> userList = mUserViewModel.getUserList();

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        for (int i = 0; i < userList.size(); i++) {
            LeaderboardUser leaderboardUser = new LeaderboardUser();
            leaderboardUser.setUserID(i + 1);
            leaderboardUser.setUsername(userList.get(i).getUsername());

            int amount = 0;

            for (int j = 0; j < appointmentList.size(); j++) {
                Appointment appointment = appointmentList.get(j);

                int year = Integer.parseInt(appointment.getAppointmentDate().substring(0,4));
                if (year == currentYear) {
                    if (appointment.getUserID() == (i + 1)) {
                        amount = amount + appointment.getBloodAmt();
                    }
                }
            }

            leaderboardUser.setBloodAmt(amount);
            mLeaderboardUserList.add(leaderboardUser);
        }

        Collections.sort(mLeaderboardUserList, new BloodAmountComparator());

        mLeaderboardAdapter = new LeaderboardAdapter(this);
        mLeaderboardAdapter.setLeaderboardUserList(mLeaderboardUserList);
        mCommunityLeaderboardBinding.aclRvYearlyTopDonorList.setAdapter(mLeaderboardAdapter);

        mCommunityLeaderboardBinding.aclRvYearlyTopDonorList.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.grid_column_count)));


    }

    public class BloodAmountComparator implements Comparator<LeaderboardUser> {
        @Override
        public int compare(LeaderboardUser u1, LeaderboardUser u2) {
            return u2.getBloodAmt() - u1.getBloodAmt();
        }
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