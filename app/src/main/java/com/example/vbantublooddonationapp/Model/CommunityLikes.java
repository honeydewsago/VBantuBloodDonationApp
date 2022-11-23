package com.example.vbantublooddonationapp.Model;

public class CommunityLikes {
    private String userType;
    private String userID;
    private String userName;
    private String userLikes;
    private String postID;

    public CommunityLikes(String userType, String userID, String userName, String userLikes, String postID) {
        this.userType = userType;
        this.userID = userID;
        this.userName = userName;
        this.userLikes = userLikes;
        this.postID = postID;
    }

    public CommunityLikes() {
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(String userLikes) {
        this.userLikes = userLikes;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }
}
