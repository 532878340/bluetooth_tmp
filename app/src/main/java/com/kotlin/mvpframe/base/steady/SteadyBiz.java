package com.kotlin.mvpframe.base.steady;

import com.base.mvp_lib.frame.manager.ApiService;
import com.base.mvp_lib.frame.manager.Callback;
import com.base.mvp_lib.frame.manager.ServiceGenerator;
import com.base.mvp_lib.frame.model.BaseBiz;

import java.util.Map;


/**
 * Created by Zijin on 2017/7/14.
 * Email:info@zijinqianbao.com
 */

public class SteadyBiz extends BaseBiz implements ISteadyContract.ISteadyModel{
    @Override
    public void request(Map<String, String> param, Callback callback) {
        performRequest(ServiceGenerator.createService(ApiService.class).getSteadyInfo(param),callback);
    }
}
