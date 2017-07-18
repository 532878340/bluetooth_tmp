package com.base.mvp_lib;

import android.app.Application;
import android.content.Context;

/**
 * Created by Zijin on 2017/7/12.
 */

public class FrameApplication extends Application {
    private static Context mCtx;

    @Override
    public void onCreate() {
        super.onCreate();

        mCtx = this;
    }
}
