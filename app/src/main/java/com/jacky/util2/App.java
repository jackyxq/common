package com.jacky.util2;

import android.app.Application;

import com.jacky.annotations.ApplicationContext;

/**
 * Created by jacky on 2018/11/6.
 */

public class App extends Application {

    static App mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }

    @ApplicationContext(ApplicationContext.ss)
    public static App getApp() {
        return mApp;
    }
}
