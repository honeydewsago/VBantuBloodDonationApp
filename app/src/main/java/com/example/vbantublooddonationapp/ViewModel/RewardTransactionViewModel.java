package com.example.vbantublooddonationapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.Model.RewardTransaction;
import com.example.vbantublooddonationapp.Repository.RewardTransactionRepository;

import java.util.List;

public class RewardTransactionViewModel extends AndroidViewModel {
    private RewardTransactionRepository mRepository;
    private LiveData<List<RewardTransaction>> mAllRewardTransactions;

    public RewardTransactionViewModel(Application application) {
        super(application);
        mRepository = new RewardTransactionRepository(application);
        mAllRewardTransactions = mRepository.getAllRewardTransactions();
    }

    public List<RewardTransaction> getRequestById(int id){return mRepository.getRequestById(id);}
    public LiveData<List<RewardTransaction>> getRewardTransactionByRewardId(int id){return mRepository.getRewardTransactionByRewardId(id);}

    public void insertRewardTransaction(RewardTransaction rewardTransaction) {mRepository.insert(rewardTransaction);}

    public void updateRewardTransaction(RewardTransaction rewardTransaction) {mRepository.update(rewardTransaction);}

    public LiveData<List<RewardTransaction>> getAllRewardTransactions() {return mAllRewardTransactions;}

}
