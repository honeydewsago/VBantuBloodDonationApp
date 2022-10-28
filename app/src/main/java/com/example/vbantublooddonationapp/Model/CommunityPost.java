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

    public CommunityPost(int postID, int userID, int organiserID, String postDesc, String postDateTime) {
        this.postID = postID;
        this.userID = userID;
        this.organiserID = organiserID;
        this.postDesc = postDesc;
        this.postDateTime = postDateTime;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getOrganiserID() {
        return organiserID;
    }

    public void setOrganiserID(int organiserID) {
        this.organiserID = organiserID;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public String getPostDateTime() {
        return postDateTime;
    }

    public void setPostDateTime(String postDateTime) {
        this.postDateTime = postDateTime;
    }
}
