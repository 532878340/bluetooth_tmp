package com.kotlin.mvpframe.alarm;

import com.kotlin.mvpframe.BaseApplication;

import java.util.List;

/**
 * Created by Zijin on 2017/7/11.
 */

public class AlarmRemindDao {
    /**
     * 增
     * @param alarmEntity
     */
    public static void insertRemind(AlarmEntity alarmEntity){
        BaseApplication.getDaoInstance().getAlarmEntityDao().insertOrReplace(alarmEntity);
    }

    /**
     * 删
     * @param id
     */
    public static void deleteRemind(long id){
        BaseApplication.getDaoInstance().getAlarmEntityDao().deleteByKey(id);
    }

    /**
     * 改
     * @param alarmEntity
     */
    public static void updateRemind(AlarmEntity alarmEntity){
        BaseApplication.getDaoInstance().getAlarmEntityDao().update(alarmEntity);
    }

    /**
     * 查
     * @return
     */
    public static List<AlarmEntity> queryAll(){
        return BaseApplication.getDaoInstance().getAlarmEntityDao().loadAll();
    }
}
