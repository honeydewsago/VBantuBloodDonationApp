package com.example.vbantublooddonationapp.Model;

public class Comments {

    String comment;
    int userID;
    int organiserID;

    public Comments(String comment, int userID, int organiserID) {
        this.comment = comment;
        this.userID = userID;
        this.organiserID = organiserID;
    }

    public String getComment() {
        return comment;
    }

    public int getUserID() {
        return userID;
    }

    public int getOrganiserID() {
        return organiserID;
    }
}
