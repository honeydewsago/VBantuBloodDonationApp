/*package com.example.vbantublooddonationapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.Model.RewardTransaction;
import com.example.vbantublooddonationapp.Repository.RewardTransactionRepository;

import java.util.List;

public class RewardTransactionViewModel extends AndroidViewModel {
    private RewardTransactionRepository mRepository;

    public RewardTransactionViewModel(Application application) {
        super(application);
        mRepository = new RewardTransactionRepository(application);
    }

    public List<RewardTransaction> getRequestById(int id){return mRepository.getRequestById(id);}
    public LiveData<List<RewardTransaction>> getRewardTransactionByRewardId(int id){return mRepository.getRewardTransactionByRewardId(id);}
}*/
