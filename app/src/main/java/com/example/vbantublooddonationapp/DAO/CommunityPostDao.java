package com.example.vbantublooddonationapp.DAO;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.vbantublooddonationapp.Model.CommunityPost;

@Dao
public interface CommunityPostDao {

    @Insert
    void insert(CommunityPost... communityPost);

}
