package com.example.vbantublooddonationapp.Model;

public class Comments {

    private String comment;
    private String userName;
    private String organiserID;
    private String date;
    private String postID;
    private String userID;

    public Comments(String comment, String userName, String organiserID, String date, String postID, String userID) {
        this.comment = comment;
        this.userName = userName;
        this.organiserID = organiserID;
        this.date = date;
        this.postID = postID;
        this.userID = userID;
    }

    public Comments(){

    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrganiserID() {
        return organiserID;
    }

    public void setOrganiserID(String organiserID) {
        this.organiserID = organiserID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
