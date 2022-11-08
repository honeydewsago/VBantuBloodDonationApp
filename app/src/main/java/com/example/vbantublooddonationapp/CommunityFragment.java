package com.example.vbantublooddonationapp;

import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.room.Database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.Model.LeaderboardUser;
import com.example.vbantublooddonationapp.Model.OrganiserImage;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.AppointmentViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.adapter.LeaderboardAdapter;
import com.example.vbantublooddonationapp.adapter.LocationAdapter;
import com.example.vbantublooddonationapp.adapter.UrgentRequestAdapter;
import com.example.vbantublooddonationapp.databinding.FragmentCommunityBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class CommunityFragment extends Fragment {

    private FragmentCommunityBinding mCommunityBinding;

    private AppointmentViewModel mAppointmentViewModel;
    private UserViewModel mUserViewModel;
    private User mUser;
    private User mUser1;
    private User mUser2;
    private User mUser3;

    private List<LeaderboardUser> top3UserList;
    private LeaderboardAdapter mLeaderboardAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mCommunityBinding = FragmentCommunityBinding.inflate(inflater, container, false);
        return mCommunityBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAppointmentViewModel = new ViewModelProvider(this).get(AppointmentViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        top3UserList = new ArrayList<>();
        List<LeaderboardUser> leaderboardUserList = new ArrayList<>();
        List<Appointment> appointmentList = mAppointmentViewModel.getAllCompletedAppointment();

        List<User> userList = mUserViewModel.getUserList();

        for (int i=0; i < userList.size(); i++) {
            LeaderboardUser leaderboardUser = new LeaderboardUser();
            leaderboardUser.setUserID(i+1);
            leaderboardUser.setUsername(userList.get(i).getUsername());

            int amount=0;

            for (int j=0; j < appointmentList.size(); j++) {
                Appointment appointment = appointmentList.get(j);
                if (appointment.getUserID() == (i+1)) {
                    amount = amount + appointment.getBloodAmt();
                }
            }

            leaderboardUser.setBloodAmt(amount);
            leaderboardUserList.add(leaderboardUser);
        }

        Collections.sort(leaderboardUserList, new BloodAmountComparator());

        for (int i=0; i<3; i++) {
            top3UserList.add(leaderboardUserList.get(i));
        }

        mLeaderboardAdapter = new LeaderboardAdapter(getActivity());
        mLeaderboardAdapter.setLeaderboardUserList(top3UserList);
        mCommunityBinding.fcRvLeaderboard.setAdapter(mLeaderboardAdapter);

        mCommunityBinding.fcRvLeaderboard.setLayoutManager(new GridLayoutManager(view.getContext(),getResources().getInteger(R.integer.grid_column_count)));


        mCommunityBinding.fcTvViewFullRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CommunityLeaderboardActivity.class);
                startActivity(i);
            }
        });

        mCommunityBinding.fcIvViewFullRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CommunityLeaderboardActivity.class);
                startActivity(i);
            }
        });

        mCommunityBinding.fcBtnNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CommunityNewPostActivity.class);
                startActivity(i);
            }
        });
    }

    public class BloodAmountComparator implements Comparator<LeaderboardUser> {
        @Override
        public int compare(LeaderboardUser u1, LeaderboardUser u2) {
            return u2.getBloodAmt() - u1.getBloodAmt();
        }
    }
}

//initAppointmentViewModel();

//LiveData<List<Appointment>> mAppointmentCompletedList = mAppointmentViewModel.getAllCompletedAppointment();
//mUser = mAppointmentCompletedList.hasObservers();
//mCommunityNewPostBinding.acnpTvUsername.setText(mUser.getUsername());

        /*
        for (int i=0; i < userList.size(); i++) {
            LeaderboardUser leaderboardUser = new LeaderboardUser();
            leaderboardUser.setUserID(i+1);
            leaderboardUser.setUsername(userList.get(i).getUsername());

            for (int j=0; j < appointmentList.size(); j++) {
                Appointment appointment = appointmentList.get(j);
                if (appointment.getUserID() == (i+1)) {
                    leaderboardUser.setBloodAmt((leaderboardUser.getBloodAmt()+appointment.getBloodAmt()));
                }
            }

            mLeaderboardUserList.add(leaderboardUser);
        }

         */

/*
if (appointments.size() == 0) {
                    mCommunityBinding.fcCvLeaderboard.setVisibility(View.GONE);
                }
                if (appointments.size() == 1) {
                    mCommunityBinding.fcCvLeaderboard.setVisibility(VISIBLE);
                    //First place donor
                    Appointment firstDonorAppointments = appointments.get(0);
                    List<User> mUserList = mUserViewModel.getUserById(firstDonorAppointments.userID);
                    mUser = mUserList.get(0);
                    mCommunityBinding.fcTvFirstPlaceUsername.setText(mUser.getUsername());
                    mCommunityBinding.fcTvFirstPlaceBloodAmount.setText(firstDonorAppointments.getBloodAmt());

                    //Hide second place donor
                    mCommunityBinding.fcTvSecondPlaceBloodAmount.setVisibility(View.GONE);
                    mCommunityBinding.fcTvSecondPlaceNo.setVisibility(View.GONE);
                    mCommunityBinding.fcTvSecondPlaceBloodAmount.setVisibility(View.GONE);
                    mCommunityBinding.fcIvSecondPlaceBackground.setVisibility(View.GONE);
                    mCommunityBinding.fcIvSecondPlaceNoLine.setVisibility(View.GONE);

                    //Hide third place donor
                    mCommunityBinding.fcTvThirdPlaceBloodAmount.setVisibility(View.GONE);
                    mCommunityBinding.fcTvThirdPlaceNo.setVisibility(View.GONE);
                    mCommunityBinding.fcTvThirdPlaceBloodAmount.setVisibility(View.GONE);
                    mCommunityBinding.fcIvThirdPlaceBackground.setVisibility(View.GONE);
                    mCommunityBinding.fcIvThirdPlaceNoLine.setVisibility(View.GONE);

                    //No more records
                    mCommunityBinding.fcTvViewFullRanking.setText("No more records");
                    mCommunityBinding.fcIvViewFullRanking.setVisibility(View.GONE);
                }
                if (appointments.size() == 2) {
                    mCommunityBinding.fcCvLeaderboard.setVisibility(VISIBLE);
                    //First place donor
                    Appointment firstDonorAppointments = appointments.get(0);
                    List<User> mUserList1 = mUserViewModel.getUserById(firstDonorAppointments.userID);
                    mUser1 = mUserList1.get(0);
                    mCommunityBinding.fcTvFirstPlaceUsername.setText(mUser1.getUsername());
                    mCommunityBinding.fcTvFirstPlaceBloodAmount.setText(firstDonorAppointments.getBloodAmt()+"ml");

                    //Second place donor
                    Appointment secondDonorAppointments = appointments.get(1);
                    List<User> mUserList2 = mUserViewModel.getUserById(secondDonorAppointments.userID);
                    mUser2 = mUserList2.get(0);
                    mCommunityBinding.fcTvSecondPlaceUsername.setText(mUser2.getUsername());
                    mCommunityBinding.fcTvSecondPlaceBloodAmount.setText(secondDonorAppointments.getBloodAmt()+"ml");

                    //Hide third place donor
                    mCommunityBinding.fcTvThirdPlaceBloodAmount.setVisibility(View.GONE);
                    mCommunityBinding.fcTvThirdPlaceNo.setVisibility(View.GONE);
                    mCommunityBinding.fcTvThirdPlaceBloodAmount.setVisibility(View.GONE);
                    mCommunityBinding.fcIvThirdPlaceBackground.setVisibility(View.GONE);
                    mCommunityBinding.fcIvThirdPlaceNoLine.setVisibility(View.GONE);
                    mCommunityBinding.fcTvThirdPlaceNo.setVisibility(View.GONE);
                    mCommunityBinding.fcIvThirdPlaceProfilePicture.setVisibility(View.GONE);
                    mCommunityBinding.fcTvThirdPlaceUsername.setVisibility(View.GONE);
                    mCommunityBinding.fcIvThirdPlaceNo.setVisibility(View.GONE);

                    //No more records
                    mCommunityBinding.fcTvViewFullRanking.setText("No more records");
                    mCommunityBinding.fcIvViewFullRanking.setVisibility(View.GONE);

                } else {
                    mCommunityBinding.fcCvLeaderboard.setVisibility(VISIBLE);
                    //First place donor
                    Appointment firstDonorAppointments = appointments.get(0);
                    List<User> mUserList1 = mUserViewModel.getUserById(firstDonorAppointments.userID);
                    mUser1 = mUserList1.get(0);
                    mCommunityBinding.fcTvFirstPlaceUsername.setText(mUser1.getUsername());
                    mCommunityBinding.fcTvFirstPlaceBloodAmount.setText(firstDonorAppointments.getBloodAmt()+"ml");

                    //Second place donor
                    Appointment secondDonorAppointments = appointments.get(1);
                    List<User> mUserList2 = mUserViewModel.getUserById(secondDonorAppointments.userID);
                    mUser2 = mUserList2.get(0);
                    mCommunityBinding.fcTvSecondPlaceUsername.setText(mUser2.getUsername());
                    mCommunityBinding.fcTvSecondPlaceBloodAmount.setText(secondDonorAppointments.getBloodAmt()+"ml");

                    //Hide third place donor
                    Appointment thirdDonorAppointments = appointments.get(2);
                    List<User> mUserList3 = mUserViewModel.getUserById(thirdDonorAppointments.userID);
                    mUser3 = mUserList3.get(0);
                    mCommunityBinding.fcTvSecondPlaceUsername.setText(mUser3.getUsername());
                    mCommunityBinding.fcTvSecondPlaceBloodAmount.setText(thirdDonorAppointments.getBloodAmt()+"ml");
                }

private void initAppointmentViewModel() {
        //initialise view model
        mAppointmentViewModel = new ViewModelProvider(this).get(AppointmentViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        //create observer object
        final Observer<List<Appointment>> appointmentListObserver = new Observer<List<Appointment>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(List<Appointment> appointments) {

            }
        };

        //add an observer for the LiveData returned by
        mAppointmentViewModel.getAllCompletedAppointment().observe(getViewLifecycleOwner(), appointmentListObserver);

    }
 */