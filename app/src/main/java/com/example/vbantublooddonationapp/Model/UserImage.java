package com.example.vbantublooddonationapp.Model;

public class UserImage {

    private String userID;
    private String url;

    public UserImage(){
    }

    public UserImage(String userID, String url) {
        this.userID = userID;
        this.url = url;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
