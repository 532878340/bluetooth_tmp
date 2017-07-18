package com.base.mvp_lib.frame.manager;

/**
 * Created by Zijin on 2017/7/12.
 */

public interface IPresenterLiftCycle {
    void onCreate();

    void onStart();

    void onRestart();

    void onResume();

    void onPause();

    void onStop();

    void onDestory();
}
