/*package com.example.vbantublooddonationapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.Model.Reward;
import com.example.vbantublooddonationapp.Repository.RewardRepository;

import java.util.List;

public class RewardViewModel extends AndroidViewModel {
    private RewardRepository mRepository;
    private LiveData<List<Reward>> mAllRewards;

    public RewardViewModel(Application application){
        super(application);
        mRepository = new RewardRepository(application);
    }

    public List<Reward> getRequestById(int id){return mRepository.getRequestById(id);}
    public LiveData<List<Reward>> getRewardByUserId(int id){return mRepository.getRewardByUserId(id);}
}
*/