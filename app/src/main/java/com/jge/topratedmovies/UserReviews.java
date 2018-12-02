package com.jge.topratedmovies;

import com.google.gson.annotations.SerializedName;

public class UserReviews {


    @SerializedName("content")
    private String content;

    @SerializedName("author")
    private String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}