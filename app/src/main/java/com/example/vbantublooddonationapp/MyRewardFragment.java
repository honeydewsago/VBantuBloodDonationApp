package com.example.vbantublooddonationapp;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRewardTransactionAdapter = new RewardTransactionAdapter(getActivity());
        binding.fmyRv.setAdapter(mRewardTransactionAdapter);
        binding.fmyRv.setLayoutManager(new GridLayoutManager(view.getContext(),1));
        initRewardTransactionViewModel();
    }
    private void initRewardTransactionViewModel(){
        mRewardTransactionViewModel = new ViewModelProvider(this).get(RewardTransactionViewModel.class);

        final Observer<List<RewardTransaction>> rewardTransListObserver = new Observer<List<RewardTransaction>>() {
            @Override
            public void onChanged(List<RewardTransaction> rewardTransactions) {mRewardTransactionAdapter.setRewardTransList(rewardTransactions);}
        };
        mRewardTransactionViewModel.getAllRewardTransactions().observe(getViewLifecycleOwner(),rewardTransListObserver);

        mRewardViewModel = new ViewModelProvider(this).get(RewardViewModel.class);
        Observer<List<Reward>> rewardListObserver = new Observer<List<Reward>>() {
            @Override
            public void onChanged(List<Reward> rewards) {
                mRewardAdapter.setRewardList(rewards);
            }
        };
        mRewardViewModel.getAllRewards().observe(getViewLifecycleOwner(),rewardListObserver);
    }
}
