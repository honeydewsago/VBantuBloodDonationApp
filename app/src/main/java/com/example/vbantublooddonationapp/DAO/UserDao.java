package com.example.vbantublooddonationapp.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vbantublooddonationapp.Model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User...user);

    @Update
    void update(User...user);

    @Query("Select * from user_table where email=(:email) and password=(:password)")
    List<User> loginUser(String email, String password);

    @Query("Select * from user_table where userID=(:id)")
    List<User> getUserById(int id);

    @Query("Select email from user_table")
    List<String> getAllUserEmails();

    @Query("Select * from user_table ORDER By userID")
    LiveData<List<User>> getAllUsers();

    @Query("Select * from user_table ORDER By userID")
    List<User> getUserList();
}