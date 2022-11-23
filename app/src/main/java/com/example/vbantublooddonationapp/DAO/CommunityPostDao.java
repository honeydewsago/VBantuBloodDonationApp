package com.example.vbantublooddonationapp.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vbantublooddonationapp.Model.CommunityPost;

import java.util.List;

@Dao
public interface CommunityPostDao {

    @Insert
    void insertCommunityPost(CommunityPost... communityPost);

    @Query("SELECT * from community_post_table where active=(1) ORDER BY postID DESC ")
    List<CommunityPost> getAllCommunityPost();

    @Query("Select * from community_post_table where postID=(:id)")
    List<CommunityPost> getCommunityPostByID(int id);

    @Query("Select * from community_post_table where postDateTime=(:postDateTime) AND postDesc=(:postDesc)")
    List<CommunityPost> getCommunityPostByDateTimePostDesc(String postDateTime, String postDesc);

    @Update
    void updateCommunityPost(CommunityPost communityPost);

}
