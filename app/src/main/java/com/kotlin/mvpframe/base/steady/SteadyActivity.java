package com.kotlin.mvpframe.base.steady;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.base.mvp_lib.frame.tmp.ListEntity;
import com.base.mvp_lib.frame.tmp.SteadyEntity;
import com.base.mvp_lib.frame.view.BaseActivity;
import com.base.mvp_lib.view.recycleview.BaseRecycleAdapter;
import com.base.mvp_lib.view.recycleview.BaseRecycleHolder;
import com.base.mvp_lib.view.recycleview.refresh.OnLoadMoreListener;
import com.base.mvp_lib.view.recycleview.refresh.RecycleLoadLayout;
import com.base.mvp_lib.view.recycleview.refresh.Status;
import com.kotlin.mvpframe.R;
import com.kotlin.mvpframe.base.steady.ISteadyContract.ISteadyView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zijin on 2017/7/14.
 * Email:info@zijinqianbao.com
 */

public class SteadyActivity extends BaseActivity<SteadyPresenter,ISteadyView> implements ISteadyView{
    private BaseRecycleAdapter mAdapter;
    private RecycleLoadLayout recycleLoadLayout;
    private SwipeRefreshLayout refresh;

    private int mCurrPage = 1;

    @NonNull
    @Override
    protected SteadyPresenter getPresenter() {
        return new SteadyPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_recycle);
        setTitle("RecycleView");

        refresh = findViewById(R.id.refresh);
        recycleLoadLayout = findViewById(R.id.loadLayout);

        recycleLoadLayout.setLoadMoreEnable(true);
        recycleLoadLayout.setLayoutManager(new LinearLayoutManager(this));
        recycleLoadLayout.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        init();
    }

    void init(){
        mAdapter = new BaseRecycleAdapter<SteadyEntity>(this,new ArrayList<SteadyEntity>(),R.layout.item_home) {
            @Override
            public void onBindView(BaseRecycleHolder holder, int position, SteadyEntity steadyEntity) {
                TextView textView = holder.getView(R.id.id_num);
                textView.setText(steadyEntity.getProductInformationName());
            }
        };

        recycleLoadLayout.setAdapter(mAdapter);
        recycleLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mCurrPage ++;
                mPresenter.performRequest(getRequestParams());
            }
        });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrPage = 1;
                mPresenter.performRequest(getRequestParams());
            }
        });

        mPresenter.performRequest(getRequestParams());
    }

    @Override
    public void updateView(ListEntity listEntity) {
        if(mCurrPage == 1){
            mAdapter.clear();
        }
        mAdapter.addAll(listEntity.getList());
        recycleLoadLayout.setLoadMoreEnable(mAdapter.getItemCount() < listEntity.getRecordCount());
    }

    @Override
    public void resetRefresh() {
        refresh.setRefreshing(false);
        recycleLoadLayout.setStatus(Status.STATUS_NO_LOADING);
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String,String> _param = new HashMap<>();
        _param.put("reqPageNum",mCurrPage + "");
        _param.put("maxResults","18");
        return _param;
    }
}
