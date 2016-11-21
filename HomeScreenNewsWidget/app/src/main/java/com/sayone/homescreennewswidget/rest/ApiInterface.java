package com.sayone.homescreennewswidget.rest;


import com.sayone.homescreennewswidget.model.BaseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sayone on 8/11/16.
 */

public interface ApiInterface {

    @GET("v1/articles")
    Call<BaseModel> getData(@Query("source") String source, @Query("sortBy") String sort_by, @Query("apiKey") String api_key);
}
