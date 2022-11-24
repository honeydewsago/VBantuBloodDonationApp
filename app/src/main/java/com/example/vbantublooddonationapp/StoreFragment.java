package com.example.vbantublooddonationapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

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
        mRewardViewModel = new ViewModelProvider(this).get(RewardViewModel.class);
        List<Reward> mRewardList = mRewardViewModel.getAllRewards();
        mRewardAdapter =  new RewardAdapter(getActivity());
        mRewardAdapter.setRewardList(mRewardList);
        binding.fsRv.setAdapter(mRewardAdapter);
        binding.fsRv.setLayoutManager(new LinearLayoutManager(getContext()));


    }


}
