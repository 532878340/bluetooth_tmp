package com.base.mvp_lib.frame.manager;

import com.base.mvp_lib.frame.entity.ResultEntity;

/**
 * Created by Zijin on 2017/7/12.
 */

public interface Callback<T> {
    /**
     * 请求成功
     */
    void onRequestSuccess(ResultEntity<T> entity);

    /**
     * 请求非正常
     */
    void onRequestIllegal(ResultEntity<T> entity);

    /**
     * 请求完成
     */
//    void onRequestFinished();

    /**
     * 请求失败、报错
     */
    void onRequestError();
}
