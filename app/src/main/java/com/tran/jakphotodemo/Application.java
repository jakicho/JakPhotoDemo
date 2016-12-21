package com.tran.jakphotodemo;

import com.tran.jakphotodemo.network.PhotoComponent;


public class Application extends android.app.Application {

    protected static Application application;
    protected PhotoComponent photoComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //photoComponent = DaggerPhotoComponent.builder().build();
    }

    public static Application app() {
        return application;
    }

    public PhotoComponent photoComponent() {
        return photoComponent;
    }

}