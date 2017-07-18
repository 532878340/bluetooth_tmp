package com.kotlin.mvpframe.base.steady;

import com.base.mvp_lib.frame.contracts.IBaseContracts;
import com.base.mvp_lib.frame.manager.Callback;
import com.base.mvp_lib.frame.tmp.ListEntity;

import java.util.Map;

/**
 * Created by Zijin on 2017/7/14.
 * Email:info@zijinqianbao.com
 */

public interface ISteadyContract {
    interface ISteadyView extends IBaseContracts.IBaseView{
        void updateView(ListEntity listEntity);

        void resetRefresh();
    }

    interface ISteadyModel extends IBaseContracts.IBaseModel{
        void request(Map<String,String> param,Callback callback);
    }
}
