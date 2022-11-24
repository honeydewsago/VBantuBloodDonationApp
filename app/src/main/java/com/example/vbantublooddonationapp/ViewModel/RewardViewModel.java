package com.example.vbantublooddonationapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.Model.Reward;
import com.example.vbantublooddonationapp.Repository.RewardRepository;

import java.util.List;

public class RewardViewModel extends AndroidViewModel {
    private RewardRepository mRepository;


    public RewardViewModel(Application application){
        super(application);
        mRepository = new RewardRepository(application);

    }

    public List<Reward> getRequestById(int id){return mRepository.getRequestById(id);}
    public LiveData<List<Reward>> getRewardByUserId(int id){return mRepository.getRewardByUserId(id);}
    public void updateReward(Reward reward) {
        mRepository.update(reward);
    }

    public List<Reward> getAllRewards() {return mRepository.getAllReward();}
}