package com.example.zcc.zxuc;

import android.app.Application;
import android.content.Context;

/**
 * Created by zcc on 2016/10/23.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        context=getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }


}
