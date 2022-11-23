package com.example.vbantublooddonationapp.Model;

public class CommunityPosts {
    public String postID;
    public String userID;
    public String userName;
    public String organiserID;
    public String postDesc;
    public String dateTime;
    public String active;
    public String url;

    public CommunityPosts(String postID, String userID, String organiserID, String postDesc, String dateTime, String active) {
        this.postID = postID;
        this.userID = userID;
        this.organiserID = organiserID;
        this.postDesc = postDesc;
        this.dateTime = dateTime;
        this.active = active;
        this.userName = userName;
        this.url = url;
    }

    public CommunityPosts(){

    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOrganiserID() {
        return organiserID;
    }

    public void setOrganiserID(String organiserID) {
        this.organiserID = organiserID;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public String getdateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}