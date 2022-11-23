package com.example.vbantublooddonationapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.Model.CommunityPosts;
//import com.example.vbantublooddonationapp.Model.CommunityPost;
import com.example.vbantublooddonationapp.Model.LeaderboardUser;
//import com.example.vbantublooddonationapp.Model.OrganiserImage;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.AppointmentViewModel;
//import com.example.vbantublooddonationapp.ViewModel.CommunityPostViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
//import com.example.vbantublooddonationapp.adapter.CommunityLikesAdapter;
//import com.example.vbantublooddonationapp.adapter.CommunityPostAdapter;
import com.example.vbantublooddonationapp.adapter.LeaderboardAdapter;
import com.example.vbantublooddonationapp.databinding.FragmentCommunityBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class CommunityFragment extends Fragment {

    private FragmentCommunityBinding mCommunityBinding;

    private AppointmentViewModel mAppointmentViewModel;
    private UserViewModel mUserViewModel;
    private List<LeaderboardUser> top3UserList;
    private LeaderboardAdapter mLeaderboardAdapter;
    //private CommunityPostAdapter mCommunityPostAdapter;
    //private CommunityPostViewModel mCommunityPostViewModel;
    private List<CommunityPosts> mCommunityPostsList;


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
            leaderboardUserList.add(leaderboardUser);
        }

        Collections.sort(leaderboardUserList, new BloodAmountComparator());

        for (int i = 0; i < 3; i++) {
            top3UserList.add(leaderboardUserList.get(i));
        }

        mLeaderboardAdapter = new LeaderboardAdapter(getActivity());
        mLeaderboardAdapter.setLeaderboardUserList(top3UserList);
        mCommunityBinding.fcRvLeaderboard.setAdapter(mLeaderboardAdapter);

        mCommunityBinding.fcRvLeaderboard.setLayoutManager(new GridLayoutManager(view.getContext(), getResources().getInteger(R.integer.grid_column_count)));

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

        /*
        mCommunityPostViewModel = new ViewModelProvider(this).get(CommunityPostViewModel.class);
        List<CommunityPost> communityPostList = mCommunityPostViewModel.getAllCommunityPost();

        mCommunityImageList = new ArrayList<CommunityImage>();
        mCommunityPostAdapter = new CommunityPostAdapter(getActivity(), mCommunityImageList);
        mCommunityPostAdapter.setCommunityPostList(communityPostList);


         */
        //mCommunityBinding.fcRvCommunityPosts.setAdapter(mCommunityPostAdapter);
        //mCommunityBinding.fcRvCommunityPosts.setLayoutManager(new GridLayoutManager(view.getContext(), getResources().getInteger(R.integer.grid_column_count)));

    }

    public class BloodAmountComparator implements Comparator<LeaderboardUser> {
        @Override
        public int compare(LeaderboardUser u1, LeaderboardUser u2) {
            return u2.getBloodAmt() - u1.getBloodAmt();
        }
    }
}