package com.example.vbantublooddonationapp;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.ViewModel.AppointmentViewModel;
import com.example.vbantublooddonationapp.databinding.FragmentCommunityBinding;

import java.util.List;


public class CommunityFragment extends Fragment {

    private FragmentCommunityBinding binding;

    private AppointmentViewModel mAppointmentViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCommunityBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initAppointmentViewModel();

        LiveData<List<Appointment>> mAppointmentCompletedList = mAppointmentViewModel.getAllCompletedAppointment();
        //mUser = mAppointmentCompletedList.hasObservers();
        //mCommunityNewPostBinding.acnpTvUsername.setText(mUser.getUsername());

        binding.fcTvViewFullRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),CommunityLeaderboardActivity.class);
                startActivity(i);
            }
        });

        binding.fcIvViewFullRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),CommunityLeaderboardActivity.class);
                startActivity(i);
            }
        });

        binding.fcBtnNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),CommunityNewPostActivity.class);
                startActivity(i);
            }
        });
    }

    private void initAppointmentViewModel() {
        //initialise view model
        mAppointmentViewModel = new ViewModelProvider(this).get(AppointmentViewModel.class);

        //create observer object
        final Observer<List<Appointment>> appointmentListObserver = new Observer<List<Appointment>>() {
            @Override
            public void onChanged(List<Appointment> appointments) {
                if (appointments.size() == 0){
                    //mNoResult.setVisibility(View.VISIBLE);
                    //mRecyclerView.setVisibility(View.GONE);
                }
                else{
                    //show in recycleView
                    //mAdapter.setWordList(appointments);
                    // Log.d("after setWordList",words.get(0).
                    //mRecyclerView.setVisibility(View.VISIBLE);
                    //mNoResult .setVisibility(View.GONE);
                }
            }
        };

        //add an observer for the LiveData returned by
        mAppointmentViewModel.getAllCompletedAppointment().observe(getViewLifecycleOwner(),appointmentListObserver);


    }
}