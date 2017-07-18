package com.base.mvp_lib.frame.contracts;

import com.base.mvp_lib.frame.manager.Callback;

import java.util.Map;

import rx.Observable;

/**
 * Created by Zijin on 2017/7/12.
 */

public interface IBaseContracts {
    /**
     * View
     */
    interface IBaseView extends IContracts.IView {
        /**
         * 显示初始化loading
         */
        void initLoading();

        /**
         * 显示加载进度框
         */
        void showLoading();

        /**
         * 隐藏通用loading
         */
        void hideLoading();

        /**
         * 显示加载失败界面
         */
        void showError();

        /**
         * 显示Toast
         */
        void showToastMsg(String message);

        /**
         * 返回请求参数
         */
        Map<String,String> getRequestParams();
    }

    /**
     * Model
     */
    interface IBaseModel extends IContracts.IModel {
        <T> void performRequest(Observable observable, Callback<T> callback);
    }
}
