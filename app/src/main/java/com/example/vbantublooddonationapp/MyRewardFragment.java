package com.example.vbantublooddonationapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.vbantublooddonationapp.Model.Reward;
import com.example.vbantublooddonationapp.Model.RewardTransaction;
import com.example.vbantublooddonationapp.ViewModel.RewardTransactionViewModel;
import com.example.vbantublooddonationapp.ViewModel.RewardViewModel;
import com.example.vbantublooddonationapp.adapter.RewardAdapter;
import com.example.vbantublooddonationapp.adapter.RewardTransactionAdapter;
import com.example.vbantublooddonationapp.databinding.FragmentMyRewardsBinding;

import java.util.List;

public class MyRewardFragment extends Fragment {
    private FragmentMyRewardsBinding binding;
    private RewardViewModel mRewardViewModel;
    private RewardAdapter mRewardAdapter;
    private RewardTransactionViewModel mRewardTransactionViewModel;
    private RewardTransactionAdapter mRewardTransactionAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyRewardsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRewardTransactionViewModel = new ViewModelProvider(this).get(RewardTransactionViewModel.class);
        mRewardViewModel = new ViewModelProvider(this).get(RewardViewModel.class);
        List<RewardTransaction> mRewardTransList = mRewardTransactionViewModel.getAllRewards();
        mRewardTransactionAdapter = new RewardTransactionAdapter(getActivity());
        mRewardTransactionAdapter.setRewardTransList(mRewardTransList);
        List<Reward> mRewardList = mRewardViewModel.getAllRewards();
        mRewardAdapter =  new RewardAdapter(getActivity());
        mRewardAdapter.setRewardList(mRewardList);
        binding.fmyRv.setAdapter(mRewardTransactionAdapter);
        binding.fmyRv.setLayoutManager(new GridLayoutManager(view.getContext(),1));
    }

}
