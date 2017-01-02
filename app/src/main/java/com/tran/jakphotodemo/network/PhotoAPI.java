package com.tran.jakphotodemo.network;


import com.tran.jakphotodemo.models.Photo;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface PhotoAPI {

    // getting data by using RXJAVA
    @GET("urlwrap/")
    Observable<List<Photo>> getPhotosListRX(
            @Query("q") String appId,
            @Query("action") String action);


}
