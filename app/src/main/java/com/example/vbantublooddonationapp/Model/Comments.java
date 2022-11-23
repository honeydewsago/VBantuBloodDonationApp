package com.example.vbantublooddonationapp.Model;

public class Comments {

    String comment = "";
    String userName = "";
    int organiserID = 0;
    String date = "";
    int postID = 0;
    int userID = 0;

    public Comments(String comment, String userName, int organiserID, String date, int postID, int userID) {
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

    public int getOrganiserID() {
        return organiserID;
    }

    public void setOrganiserID(int organiserID) {
        this.organiserID = organiserID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
