package com.tran.jakphotodemo;

import com.tran.jakphotodemo.network.DaggerPhotoComponent;
import com.tran.jakphotodemo.network.PhotoComponent;
import com.tran.jakphotodemo.network.PhotoModule;


public class Application extends android.app.Application {

    protected static Application application;
    protected PhotoComponent photoComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        photoComponent = DaggerPhotoComponent.builder()
                .photoModule(new PhotoModule(getBaseContext()))
                .build();
    }

    public static Application app() {
        return application;
    }

    public PhotoComponent photoComponent() {
        return photoComponent;
    }

}