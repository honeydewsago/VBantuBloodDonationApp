package com.example.vbantublooddonationapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.Model.Reward;
import com.example.vbantublooddonationapp.Repository.RewardRepository;

import java.util.List;

public class RewardViewModel extends AndroidViewModel {
    private RewardRepository mRepository;

    //constructor
    public RewardViewModel(Application application){
        super(application);
        mRepository = new RewardRepository(application);

    }
    //get reward with id
    public List<Reward> getRequestById(int id){return mRepository.getRequestById(id);}
    //get live data with id
    public LiveData<List<Reward>> getRewardByUserId(int id){return mRepository.getRewardByUserId(id);}
    //update database
    public void updateReward(Reward reward) {
        mRepository.update(reward);
    }
    //getting all rewards from database
    public List<Reward> getAllRewards() {return mRepository.getAllReward();}
}
