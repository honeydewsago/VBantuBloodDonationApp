package com.example.vbantublooddonationapp.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.vbantublooddonationapp.Model.User;

@Dao
public interface UserDao {

    @Insert
    void insert(User...user);

}