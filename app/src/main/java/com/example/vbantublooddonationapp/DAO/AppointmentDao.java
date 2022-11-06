package com.example.vbantublooddonationapp.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.vbantublooddonationapp.Model.Appointment;

import java.util.List;

@Dao
public interface AppointmentDao {

    @Insert
    void insert(Appointment... appointments);

    @Query("Select * from appointment_table where userID=(:id)")
    List<Appointment> getRequestByUserId(int id);

}
