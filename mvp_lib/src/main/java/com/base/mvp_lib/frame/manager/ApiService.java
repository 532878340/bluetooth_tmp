package com.base.mvp_lib.frame.manager;

import com.base.mvp_lib.frame.constants.RouteConstants;
import com.base.mvp_lib.frame.entity.ResultEntity;
import com.base.mvp_lib.frame.tmp.ListEntity;
import com.base.mvp_lib.frame.tmp.User;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Zijin on 2017/7/12.
 */

public interface ApiService {
    @GET(RouteConstants.INDEX)
    Observable<ResultEntity<User>> platformIndex();

    @GET(RouteConstants.STEADY_INFO)
    Observable<ResultEntity<ListEntity>> getSteadyInfo(@QueryMap Map<String, String> param);
}
