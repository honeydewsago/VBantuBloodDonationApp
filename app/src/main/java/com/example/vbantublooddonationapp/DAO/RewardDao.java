package com.example.vbantublooddonationapp.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vbantublooddonationapp.Model.Reward;

import java.util.List;

@Dao
public interface RewardDao {
    @Insert
    void insert(Reward... rewards);

    @Update
    void update(Reward... rewards);

    @Query("Select * from reward_table where rewardID=(:id)")
    List<Reward> getRequestById(int id);

    @Query("Select * from reward_table where userID=(:id) ORDER By rewardID DESC")
    LiveData<List<Reward>> getRewardByUserId(int id);

}
