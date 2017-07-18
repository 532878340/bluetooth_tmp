package com.base.mvp_lib.frame.presenter;

import com.base.mvp_lib.frame.contracts.IBaseContracts;
import com.base.mvp_lib.frame.manager.IPresenterLiftCycle;

import java.util.Map;

/**
 * Created by Zijin on 2017/7/12.
 * Email:info@zijinqianbao.com
 */

public abstract class BasePresenter<V extends IBaseContracts.IBaseView, M extends IBaseContracts.IBaseModel> extends FrameRootPresenter<V, M> implements IPresenterLiftCycle {
    @Override
    public void onCreate() {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onRestart() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onDestory() {
    }

    /**
     * 网络请求
     */
    protected abstract void performRequest(Map<String,String> param);

    /**
     * 网络请求失败
     */
    public void onRequestError() {
        if (isViewAttach()) {
            getView().showError();
        }
    }
}
