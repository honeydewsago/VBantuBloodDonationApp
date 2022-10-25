package com.example.vbantublooddonationapp.DAO;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.vbantublooddonationapp.Model.BloodRequest;

@Dao
public interface BloodRequestDao {

    @Insert
    void insert(BloodRequest...bloodRequest);
}
