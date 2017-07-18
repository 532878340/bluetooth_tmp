package com.kotlin.mvpframe.base.steady;

import com.base.mvp_lib.frame.entity.ResultEntity;
import com.base.mvp_lib.frame.manager.Callback;
import com.base.mvp_lib.frame.presenter.BasePresenter;
import com.base.mvp_lib.frame.tmp.ListEntity;

import java.util.Map;

/**
 * Created by Zijin on 2017/7/14.
 * Email:info@zijinqianbao.com
 */

public class SteadyPresenter extends BasePresenter<ISteadyContract.ISteadyView,ISteadyContract.ISteadyModel>{
    @Override
    protected ISteadyContract.ISteadyModel getModel() {
        return new SteadyBiz();
    }

    @Override
    protected void performRequest(Map<String, String> param) {
        mModel.request(param, new Callback<ListEntity>() {
            @Override
            public void onRequestSuccess(ResultEntity<ListEntity> entity) {
                if(isViewAttach()){
                    getView().updateView(entity.getData());
                    getView().resetRefresh();
                }
            }

            @Override
            public void onRequestIllegal(ResultEntity<ListEntity> entity) {
                if(isViewAttach()){
                    getView().showToastMsg(entity.getDescription());
                    getView().resetRefresh();
                }
            }

            @Override
            public void onRequestError() {
                SteadyPresenter.this.onRequestError();
                getView().resetRefresh();
            }
        });
    }
}
