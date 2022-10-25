package com.example.vbantublooddonationapp.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vbantublooddonationapp.Model.CommunityPost;

import java.util.List;

@Dao
public interface CommunityPostDao {

    @Insert
    void insertCommunityPost(CommunityPost... communityPost);

    @Query("SELECT * from community_post_table ORDER BY postID")
    LiveData<List<CommunityPost>> getAllCommunityPost();

    @Update
    void updateCommunityPost(CommunityPost communityPost);

    @Delete
    void deleteCommunityPost(CommunityPost communityPost);

}
