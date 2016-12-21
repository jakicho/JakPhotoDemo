package com.tran.jakphotodemo.network;

import com.tran.jakphotodemo.models.Photo;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

@Module
public class PhotoModule {

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(PhotoAPI.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides @Singleton
    PhotoAPI provideWeatherApi(Retrofit retrofit) {
        return retrofit.create(PhotoAPI.class);
    }

    @Provides @Singleton
    Observable<List<Photo>> providePhotosList(PhotoAPI photoApi) {

        return photoApi.getPhotosListRX(PhotoAPI.APP_ID, PhotoAPI.ACTION);

    }
}