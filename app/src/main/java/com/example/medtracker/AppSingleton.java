package com.example.medtracker;

import android.app.Application;

public class AppSingleton extends Application {

    private static AppSingleton instance;

    public static AppSingleton getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
