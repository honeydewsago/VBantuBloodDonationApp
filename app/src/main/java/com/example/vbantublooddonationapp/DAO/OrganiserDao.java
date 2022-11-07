package com.example.vbantublooddonationapp.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;

import java.util.List;

@Dao
public interface OrganiserDao {

    @Insert
    void insert(Organiser...organiser);

    @Update
    void update(Organiser...organiser);

    @Query("Select * from organiser_table where email=(:email) and password=(:password)")
    List<Organiser> loginOrganiser(String email, String password);

    @Query("Select email from organiser_table")
    List<String> getAllOrganiserEmails();

    @Query("Select * from organiser_table ORDER By organiserID")
    LiveData<List<Organiser>> getAllOrganisers();

    @Query("Select * from organiser_table where organiserID=(:id)")
    List<Organiser> getOrganiserById(int id);
}
