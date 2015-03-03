package com.swipeme.www.swipeme;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class SwipeMeApplication extends Application {

    public static final String YOUR_APPLICATION_ID = "vhQM6PPW6eQOImqAi1ixKE12fcnM5QSzotSUI9Dh";
    public static final String YOUR_CLIENT_KEY = "DdOmhTSFB5HALDBFHdmPvjeBV6I7umDPgDEvxrTX";

    @Override
    public void onCreate() {
        super.onCreate();
        // Register your parse models here
        ParseObject.registerSubclass(Message.class);
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
    }
}
