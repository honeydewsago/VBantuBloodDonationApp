package com.example.vbantublooddonationapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.FragmentProfileBinding;

import java.util.List;

public class ProfileFragment extends Fragment {
    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private String mUserType = "user";
    private List<User> mUserList;

    private UserViewModel mUserViewModel;
    private OrganiserViewModel mOrganiserViewModel;
    private FragmentProfileBinding profileBinding;
    Toolbar toolbar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        profileBinding = FragmentProfileBinding.inflate(getLayoutInflater());

        return profileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUserViewModel();

        toolbar = (Toolbar)getView().findViewById(R.id.fp_toolbar);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        profileBinding.fpBtnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        mPreferences = getActivity().getSharedPreferences("com.example.vbantublooddonationapp",MODE_PRIVATE);

        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)) {
            mUserID = mPreferences.getInt(USERID_KEY,1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        List<User> mUserList = mUserViewModel.getUserById(mUserID);

        User user = mUserList.get(0);

        profileBinding.fpTvUsername.setText(user.username);
        profileBinding.fpTvEmail.setText(user.email);
        profileBinding.fpTvIC.setText(user.icNo);
        profileBinding.fpTvDOB.setText(user.dateOfBirth);
        profileBinding.fpTvBloodGroup.setText(user.bloodType);

        if (mUserType.equals("organiser")) {
            profileBinding.fpIvQrCode.setVisibility(View.GONE);
            profileBinding.fpTvAppointmentHistory.setVisibility(View.GONE);
            profileBinding.fpTvRewards.setVisibility(View.GONE);
            profileBinding.fpTvUserAppointments.setVisibility(View.VISIBLE);
        }
        else {
            profileBinding.fpTvUserAppointments.setVisibility(View.GONE);
        }

        profileBinding.fpIvQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProfileQrActivity.class));
            }
        });

        profileBinding.fpTvAppointmentHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AppointmentHistoryActivity.class));
            }
        });

        profileBinding.fpTvRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RewardsActivity.class));
            }
        });

        profileBinding.fpTvUserAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UserAppointmentsActivity.class));
            }
        });
    }



    private void initUserViewModel(){
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        final Observer<List<User>> userListObserver = new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                mUserList = users;
            }
        };
        mUserViewModel.getAllUsers().observe(getViewLifecycleOwner(),userListObserver);
    }

}