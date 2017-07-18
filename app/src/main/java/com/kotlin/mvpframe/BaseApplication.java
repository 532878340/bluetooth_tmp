package com.kotlin.mvpframe;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.kotlin.mvpframe.alarm.DaoMaster;
import com.kotlin.mvpframe.alarm.DaoSession;

/**
 * Created by Zijin on 2017/7/11.
 */

public class BaseApplication extends Application{
    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        setupDatabase();
    }

    private void setupDatabase() {
        //创建数据库 user.db
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "user.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);

        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoInstance() {
        return daoSession;
    }
}
