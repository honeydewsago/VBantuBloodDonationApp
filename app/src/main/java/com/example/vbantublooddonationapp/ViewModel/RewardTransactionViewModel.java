package com.example.vbantublooddonationapp.ViewModel;

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

    //get request by id
    public List<RewardTransaction> getRequestById(int id){return mRepository.getRequestById(id);}
    //get reward transaction by reward id
    public LiveData<List<RewardTransaction>> getRewardTransactionByRewardId(int id){return mRepository.getRewardTransactionByRewardId(id);}

    //send repo to insert reward transaction
    public void insertRewardTransaction(RewardTransaction rewardTransaction) {mRepository.insert(rewardTransaction);}

    //send repo to update reward transaction detail
    public void updateRewardTransaction(RewardTransaction rewardTransaction) {mRepository.update(rewardTransaction);}


    //get all rewards
    public List<RewardTransaction> getAllRewards() {return mRepository.getAllRewardTransactions();}
}
