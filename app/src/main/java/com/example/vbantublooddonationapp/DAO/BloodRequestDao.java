package com.example.vbantublooddonationapp.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.vbantublooddonationapp.Model.BloodRequest;
import com.example.vbantublooddonationapp.Model.Organiser;

import java.util.List;

@Dao
public interface BloodRequestDao {

    @Insert
    void insert(BloodRequest...bloodRequest);

    @Query("Select * from blood_request_table where active=1 ORDER By organiserID DESC")
    LiveData<List<BloodRequest>> getAllActiveRequests();
}
