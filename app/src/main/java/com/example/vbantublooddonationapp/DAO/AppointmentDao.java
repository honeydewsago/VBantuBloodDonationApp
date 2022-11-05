package com.example.vbantublooddonationapp.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.Model.BloodRequest;
import com.example.vbantublooddonationapp.Model.Organiser;

import java.util.List;

@Dao
public interface AppointmentDao {

    @Insert
    void insert(Appointment... appointments);

    @Update
    void update(Appointment...appointments);

    @Query("Select * from appointment_table where organiserID=(:id) ORDER By appointmentID DESC")
    LiveData<List<Appointment>> getAppointmentByOrganiserID(int id);
}
