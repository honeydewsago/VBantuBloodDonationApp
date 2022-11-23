package com.example.vbantublooddonationapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.vbantublooddonationapp.Model.Reward;
import com.example.vbantublooddonationapp.ViewModel.RewardViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.adapter.RewardAdapter;
import com.example.vbantublooddonationapp.databinding.FragmentStoreBinding;

import java.util.List;

public class StoreFragment extends Fragment {

    private FragmentStoreBinding binding;
    private UserViewModel mUserViewModel;
    private RewardViewModel mRewardViewModel;
    private RewardAdapter mRewardAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStoreBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRewardAdapter =  new RewardAdapter(getActivity());
        binding.fsRv.setAdapter(mRewardAdapter);
        binding.fsRv.setLayoutManager(new GridLayoutManager(view.getContext(), 1));
        initRewardViewModel();
    }

    private void initRewardViewModel() {
        //initialize the view model from the OrganiserViewModel
        mRewardViewModel = new ViewModelProvider(this).get(RewardViewModel.class);

        //initialize the observer to observe the Live Data
        final Observer<List<Reward>> rewardListObserver = new Observer<List<Reward>>() {
            @Override
            public void onChanged(List<Reward> rewards) {
                mRewardAdapter.setRewardList(rewards);
            }
        };

        mRewardViewModel.getAllRewards().observe(getViewLifecycleOwner(),rewardListObserver);
    }
}
