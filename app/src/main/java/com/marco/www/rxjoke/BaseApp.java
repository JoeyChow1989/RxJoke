package com.marco.www.rxjoke;

import android.app.Application;

/**
 * Created by JDD on 2016/4/8.
 */
public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
       // LeakCanary.install(this);
    }
}
