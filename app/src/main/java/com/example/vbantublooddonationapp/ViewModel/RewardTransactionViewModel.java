package com.example.vbantublooddonationapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.Model.RewardTransaction;
import com.example.vbantublooddonationapp.Repository.RewardTransactionRepository;

import java.util.List;

public class RewardTransactionViewModel extends AndroidViewModel {
    private RewardTransactionRepository mRepository;

    //constructor
    public RewardTransactionViewModel(Application application) {
        super(application);
        mRepository = new RewardTransactionRepository(application);
    }
    //get reward with id
    public List<RewardTransaction> getRequestById(int id){return mRepository.getRequestById(id);}
    //get live data with id
    public LiveData<List<RewardTransaction>> getRewardTransactionByRewardId(int id){return mRepository.getRewardTransactionByRewardId(id);}
    //insert data into database
    public void insertRewardTransaction(RewardTransaction rewardTransaction) {mRepository.insert(rewardTransaction);}
    //update data to the database
    public void updateRewardTransaction(RewardTransaction rewardTransaction) {mRepository.update(rewardTransaction);}

    //getting all rewards trans from database
    public List<RewardTransaction> getAllRewards() {return mRepository.getAllRewardTransactions();}
}
