package com.sayone.homescreennewswidget.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sayone on 8/11/16.
 */

public class BaseModel {

    @SerializedName("status")
    private String status;

    @SerializedName("source")
    private String source;

    @SerializedName("sortBy")
    private String sortBy;

    @SerializedName("articles")
    private List<Articles> articles;

    public String getStatus() {
        return status;
    }

    public List<Articles> getArticles() {
        return articles;
    }

    public void setArticles(List<Articles> articles) {
        this.articles = articles;
    }

}
