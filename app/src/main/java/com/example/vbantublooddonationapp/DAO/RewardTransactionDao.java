package com.example.vbantublooddonationapp.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vbantublooddonationapp.Model.RewardTransaction;

import java.util.List;

@Dao
public interface RewardTransactionDao {

    @Insert
    void insert(RewardTransaction... rewardTransaction);

    @Update
    void update(RewardTransaction... rewardTransaction);

    @Query("Select * from reward_transaction_table where reward_transID=(:id)")
    List<RewardTransaction> getRequestById(int id);

    @Query("Select * from reward_transaction_table where rewardID=(:id) ORDER By rewardID")
    LiveData<List<RewardTransaction>> getRewardsTransactionByRewardId(int id);

    @Query("Select * from reward_transaction_table")
    LiveData<List<RewardTransaction>> getAllRewardTransactions();
}
