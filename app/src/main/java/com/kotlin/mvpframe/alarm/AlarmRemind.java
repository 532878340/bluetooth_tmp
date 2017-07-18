package com.kotlin.mvpframe.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Zijin on 2017/7/11.
 */

public class AlarmRemind {
    private static final String TAG = "AlarmRemind";
    
    /**
     * 开启提醒
     */
    public static void startRemind(Context context,int hour,int minute,long id,AlarmEntity alarmEntity){
        Calendar mCalendar = Calendar.getInstance();

        long systemTime = System.currentTimeMillis();
        mCalendar.setTimeInMillis(systemTime);

        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        mCalendar.set(Calendar.HOUR_OF_DAY,hour);//提醒的时间
        mCalendar.set(Calendar.MINUTE,minute);
        mCalendar.set(Calendar.SECOND,0);
        mCalendar.set(Calendar.MILLISECOND,0);

        long selectTime = mCalendar.getTimeInMillis();//获取设定的时间
        if(systemTime > selectTime){
            mCalendar.add(Calendar.DAY_OF_MONTH,1);
        }

        long l = id;
        Intent intent = new Intent(context,AlarmReceiver.class);
        intent.putExtra("title",alarmEntity.getTitle());
        PendingIntent pi = PendingIntent.getBroadcast(context, (int) l,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //单次提醒
        if(alarmEntity.getCycle() == 0){
            am.set(AlarmManager.RTC_WAKEUP,mCalendar.getTimeInMillis(),pi);
        }else{
            //重复提醒
            am.setRepeating(AlarmManager.RTC_WAKEUP,mCalendar.getTimeInMillis() ,(1000 * 60 * 3),pi);
        }
    }

    public static void stopRemind(Context context,long id){
        long l = id;

        Intent intent = new Intent(context,AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, (int) l,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //取消警报
        am.cancel(pi);

        Log.d(TAG, "stopRemind: 关闭了提醒");
    }
}
