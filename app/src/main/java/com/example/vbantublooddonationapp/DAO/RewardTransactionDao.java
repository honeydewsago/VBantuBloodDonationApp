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

    @Query("Select * from reward_trans_table where reward_transID=(:id)")
    List<RewardTransaction> getRequestById(int id);

    @Query("Select * from reward_trans_table where rewardID=(:id) ORDER By reward_transID DESC")
    LiveData<List<RewardTransaction>> getRewardsTransactionByRewardId(int id);

    @Query("Select * from reward_trans_table ORDER BY status=(:status) DESC")
    List<RewardTransaction> getAllRewardsTransaction(String status);
}
