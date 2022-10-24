package com.example.vbantublooddonationapp.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.vbantublooddonationapp.Model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User...user);

    @Query("Select * from user_table where email=(:email) and password=(:password)")
    List<User> loginUser(String email, String password);

    @Query("Select email from user_table")
    List<String> getAllUserEmails();
}