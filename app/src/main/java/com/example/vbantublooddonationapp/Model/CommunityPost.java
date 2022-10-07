package com.example.vbantublooddonationapp.Model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "community_post_table")
public class CommunityPost {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "postID")
    public int postID;

    @ColumnInfo(name = "userID")
    public int userID;

    @ColumnInfo(name = "organiserID")
    public int organiserID;

    @ColumnInfo(name = "postDesc")
    public String postDesc;

    @ColumnInfo(name = "postDateTime")
    public String postDateTime;

    @ColumnInfo(name = "commentDesc")
    public String commentDesc;
}
