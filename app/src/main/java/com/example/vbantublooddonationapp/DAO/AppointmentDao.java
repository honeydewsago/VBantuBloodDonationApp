package com.example.vbantublooddonationapp.DAO;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.vbantublooddonationapp.Model.Appointment;

@Dao
public interface AppointmentDao {

    @Insert
    void insert(Appointment... appointments);
}
