package com.example.vbantublooddonationapp.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;

import java.util.List;

@Dao
public interface OrganiserDao {

    @Insert
    void insert(Organiser...organiser);

    @Query("Select * from organiser_table where email=(:email) and password=(:password)")
    List<Organiser> loginOrganiser(String email, String password);
}
