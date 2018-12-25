package com.ht.communi.application;

import android.app.Application;
import android.content.Context;

/**
 * 作用：全局Application
 */
public class BaseApplication extends Application {
    public static Context getmContext() {
        return sContext;
    }

    static Context sContext;
}
