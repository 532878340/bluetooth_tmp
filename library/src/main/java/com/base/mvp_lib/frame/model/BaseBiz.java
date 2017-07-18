package com.base.mvp_lib.frame.model;

import com.base.mvp_lib.frame.constants.Constants;
import com.base.mvp_lib.frame.contracts.IBaseContracts;
import com.base.mvp_lib.frame.entity.ResultEntity;
import com.base.mvp_lib.frame.manager.Callback;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Zijin on 2017/7/17.
 * Email:info@zijinqianbao.com
 */

public class BaseBiz implements IBaseContracts.IBaseModel {

    @Override
    public <T> void performRequest(Observable observable, Callback<T> callback) {
        observable.throttleFirst(Constants.THROTTLE_DELAY, TimeUnit.MILLISECONDS)
                .timeout(Constants.REQUEST_TIMEOUT,TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultEntity<T>>(){

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onRequestError();
                    }

                    @Override
                    public void onNext(ResultEntity<T> resultEntity) {
                        if (resultEntity.isOk()) {
                            callback.onRequestSuccess(resultEntity);
                        } else {
                            callback.onRequestIllegal(resultEntity);
                        }
                    }
                });

    }
}
