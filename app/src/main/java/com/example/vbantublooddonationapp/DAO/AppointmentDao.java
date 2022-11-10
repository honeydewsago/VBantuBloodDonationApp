package com.example.vbantublooddonationapp.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vbantublooddonationapp.Model.Appointment;

import java.util.List;


@Dao
public interface AppointmentDao {

    @Insert
    void insert(Appointment... appointments);

    @Query("Select * from appointment_table where userID=(:id)")
    List<Appointment> getRequestByUserId(int id);


    @Update
    void update(Appointment... appointments);

    @Query("Select * from appointment_table where organiserID=(:id) ORDER By appointmentID DESC")
    LiveData<List<Appointment>> getAppointmentByOrganiserID(int id);

    @Query("Select * from appointment_table where appointmentID=(:id)")
    List<Appointment> getAppointmentById(int id);

    @Query("SELECT * from appointment_table where status=('Completed') ORDER By bloodAmt DESC")
    List<Appointment> getAllCompletedAppointment();

    @Query("Select * from appointment_table where userID=(:id)")
    LiveData<List<Appointment>> getAppointmentByUserID(int id);
}
