package com.example.vbantublooddonationapp.Model;

public class PostParams {
    String postDateTime;
    String postDesc;

    public PostParams(String postDateTime, String postDesc) {
        this.postDateTime = postDateTime;
        this.postDesc = postDesc;
    }

    public String getPostDateTime() {
        return postDateTime;
    }

    public String getPostDesc() {
        return postDesc;
    }
}
