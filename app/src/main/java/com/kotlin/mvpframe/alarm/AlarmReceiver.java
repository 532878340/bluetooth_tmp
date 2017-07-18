package com.kotlin.mvpframe.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Zijin on 2017/7/11.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "提醒广播: " + action + " 数据：" + intent.getStringExtra("title"));

        //统一处理提醒开关
    }
}
