package com.example.vbantublooddonationapp.Model;

public class CommunityImage {
    private int postID;
    String url = "";

    public CommunityImage(int postID, String url) {
        this.url = url;
        this.postID = postID;
    }

    public CommunityImage(){

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }
}
