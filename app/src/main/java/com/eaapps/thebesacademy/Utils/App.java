package com.eaapps.thebesacademy.Utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.eaapps.thebesacademy.Activities.NoNet;

/**
 * Created by eslamandroid on 3/16/18.
 */

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        if (!Constants.CheckInternet(this)) {
            startActivity(new Intent(this, NoNet.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
