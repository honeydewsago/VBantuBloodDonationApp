package com.example.vbantublooddonationapp.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vbantublooddonationapp.Model.BloodRequest;
import com.example.vbantublooddonationapp.Model.Organiser;

import java.util.List;

@Dao
public interface BloodRequestDao {

    @Insert
    void insert(BloodRequest...bloodRequest);

    @Query("Select * from blood_request_table where active=1 ORDER By requestID DESC")
    LiveData<List<BloodRequest>> getAllActiveRequests();

    @Query("Select * from blood_request_table where requestID=(:id)")
    List<BloodRequest> getRequestById(int id);

    @Query("Select * from blood_request_table where organiserID=(:id) ORDER By requestID DESC")
    List<BloodRequest> getRequestByOrganiserId(int id);

    @Update
    void update(BloodRequest...bloodRequest);

}
