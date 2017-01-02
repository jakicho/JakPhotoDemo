package com.tran.jakphotodemo.network;

import android.content.Context;

import com.tran.jakphotodemo.R;
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

    private final Context context;

    public PhotoModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(context.getString(R.string.base_url))
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

        return photoApi.getPhotosListRX(context.getString(R.string.app_id), context.getString(R.string.action));

    }
}