package com.tran.jakphotodemo.network;

import com.tran.jakphotodemo.MainFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        PhotoModule.class,
})
public interface PhotoComponent {
    void inject(MainFragment mainFragment);
}
