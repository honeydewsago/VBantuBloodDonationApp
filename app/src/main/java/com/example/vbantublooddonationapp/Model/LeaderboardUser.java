package com.example.vbantublooddonationapp.Model;

public class LeaderboardUser {
    private int userID;
    private String username;
    private int bloodAmt;

    public LeaderboardUser() {
    }

    public LeaderboardUser(int userID, String username, int bloodAmt) {
        this.userID = userID;
        this.username = username;
        this.bloodAmt = bloodAmt;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBloodAmt() {
        return bloodAmt;
    }

    public void setBloodAmt(int bloodAmt) {
        this.bloodAmt = bloodAmt;
    }
}
