package com.example.vbantublooddonationapp;

import static android.content.Context.MODE_PRIVATE;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vbantublooddonationapp.Model.BloodRequest;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.ViewModel.BloodRequestViewModel;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.adapter.LocationAdapter;
import com.example.vbantublooddonationapp.adapter.UrgentRequestAdapter;
import com.example.vbantublooddonationapp.databinding.CardLocationBinding;
import com.example.vbantublooddonationapp.databinding.FragmentHomeBinding;

import java.util.List;


public class HomeFragment extends Fragment {

    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private String mUserType = "user";

    private FragmentHomeBinding homeBinding;
    private UrgentRequestAdapter mUrgentRequestAdapter;
    private LocationAdapter mLocationAdapter;
    private BloodRequestViewModel mBloodRequestViewModel;
    private OrganiserViewModel mOrganiserViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(getLayoutInflater());
        return homeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPreferences = getActivity().getSharedPreferences("com.example.vbantublooddonationapp",MODE_PRIVATE);

        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)) {
            mUserID = mPreferences.getInt(USERID_KEY,1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        if (mUserType.equals("organiser")) {
            homeBinding.fhIvBloodRequestHistory.setVisibility(View.VISIBLE);
        }
        else {
            homeBinding.fhIvBloodRequestHistory.setVisibility(View.GONE);
        }

        homeBinding.fhIvBloodRequestHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),BloodRequestHistory.class);
                startActivity(i);
            }
        });


        mUrgentRequestAdapter = new UrgentRequestAdapter(getActivity());
        homeBinding.fhRvUrgentRequest.setAdapter(mUrgentRequestAdapter);

        mLocationAdapter = new LocationAdapter(getActivity());
        homeBinding.fhRvLocation.setAdapter(mLocationAdapter);

        //set the layout manager
        homeBinding.fhRvUrgentRequest.setLayoutManager(new GridLayoutManager(view.getContext(),getResources().getInteger(R.integer.grid_column_count)));
        homeBinding.fhRvLocation.setLayoutManager(new GridLayoutManager(view.getContext(), getResources().getInteger(R.integer.grid_column_count)));

        initUrgentRequestViewModel();
        initOrganiserViewModel();
    }

    private void initUrgentRequestViewModel() {
        //initialize the view model from the BloodRequestViewModel
        mBloodRequestViewModel = new ViewModelProvider(this).get(BloodRequestViewModel.class);

        //initialize the observer to observe the Live Data
        final Observer<List<BloodRequest>> requestListObserver = new Observer<List<BloodRequest>>() {
            @Override
            public void onChanged(List<BloodRequest> requests) {
                mUrgentRequestAdapter.setRequestList(requests);
            }
        };

        mBloodRequestViewModel.getAllActiveRequests().observe(getViewLifecycleOwner(),requestListObserver);
    }

    private void initOrganiserViewModel() {
        //initialize the view model from the OrganiserViewModel
        mOrganiserViewModel = new ViewModelProvider(this).get(OrganiserViewModel.class);

        //initialize the observer to observe the Live Data
        final Observer<List<Organiser>> organiserListObserver = new Observer<List<Organiser>>() {
            @Override
            public void onChanged(List<Organiser> organisers) {
                mLocationAdapter.setOrganiserList(organisers);
            }
        };

        mOrganiserViewModel.getAllOrganisers().observe(getViewLifecycleOwner(),organiserListObserver);
    }
}