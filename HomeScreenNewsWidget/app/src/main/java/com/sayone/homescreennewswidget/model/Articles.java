package com.sayone.homescreennewswidget.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sayone on 8/11/16.
 */

public class Articles {

    @SerializedName("author")
    private String author;

    @SerializedName("description")
    private String description;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("urlToImage")
    private String urlToImage;

    @SerializedName("publishedAt")
    private String publishedAt;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }


    public String getPublishedAt() {
        return publishedAt;
    }

    public String getUrlToImage() {
        return urlToImage;
    }
}
