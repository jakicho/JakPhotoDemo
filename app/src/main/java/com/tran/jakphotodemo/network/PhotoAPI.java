package com.tran.jakphotodemo.network;


import com.tran.jakphotodemo.models.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface PhotoAPI {

    String BASE_URL = "http://webdefence.global.blackspider.com";
    String APP_ID = "AXicHcw7DsMgEADRPRFgOY4lqkTp3Ll1hxYscIBFC47kK6bKKXKOfKZ90sANnm8A9wLgeHTKisoPkUyISLkxRYGUYO-n677UTXUnde4BDw5fMJbYtUvFtLLJ6MTK4FsrWsqtUi7RoPMUrWPRjhKQrPvdZPHUqMI0x7R4fR-sHtU4dPDvA0qZMFQ";
    String ACTION = "allow";

    // getting data
    @GET("urlwrap/")
    Call<List<Photo>> getPhotosList(
            @Query("q") String appId,
            @Query("action") String action);

    // getting data by using RXJAVA
    @GET("urlwrap/")
    Observable<List<Photo>> getPhotosListRX(
            @Query("q") String appId,
            @Query("action") String action);


}
