package com.kotlin.mvpframe.recycleview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.TextView;

import com.base.mvp_lib.view.recycleview.BaseRecycleAdapter;
import com.base.mvp_lib.view.recycleview.BaseRecycleHolder;
import com.base.mvp_lib.view.recycleview.DividerGridItemDecoration;
import com.base.mvp_lib.view.recycleview.refresh.OnLoadMoreListener;
import com.base.mvp_lib.view.recycleview.refresh.RecycleLoadLayout;
import com.base.mvp_lib.view.recycleview.refresh.Status;
import com.kotlin.mvpframe.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zijin on 2017/7/13.
 */

public class RecycleActivity extends AppCompatActivity {
    private static final String TAG = "RecycleActivity";

    private SwipeRefreshLayout refresh;

    private List<String> mDatas;
    private BaseRecycleAdapter mAdapter;

    private RecycleLoadLayout recycleLoadLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_recycle);
        setTitle("RecycleView");

        init();

        refresh = findViewById(R.id.refresh);
        recycleLoadLayout = findViewById(R.id.loadLayout);
        recycleLoadLayout.setLoadMoreEnable(true);
        recycleLoadLayout.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        recycleLoadLayout.addItemDecoration(new DividerGridItemDecoration(this));

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.clear();
                        mAdapter.addAll(mDatas);

                        recycleLoadLayout.setLoadMoreEnable(true);
                        refresh.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        mAdapter = new BaseRecycleAdapter<String>(this, mDatas, R.layout.item_home) {
            @Override
            public void onBindView(BaseRecycleHolder holder, int position, String s) {
                TextView textView = holder.getView(R.id.id_num);
                textView.setText(s);
            }
        };

        recycleLoadLayout.setAdapter(mAdapter);
        recycleLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //设置刷新状态,请求网络数据
                refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onsuccess();
                    }
                }, 3000);
            }
        });
    }

    private void init() {
        mDatas = new ArrayList<>();
        for (int i = 'A'; i <= 'Z'; i++) {
            mDatas.add("" + (char) i);
        }
    }

    void onsuccess(){
        List<String> list = new ArrayList<>();
        int count = mAdapter.getItemCount();

        for (int i = count; i < count + 10 ; i++) {
            list.add("加载的" + i);
        }
        mAdapter.addAll(list);

        recycleLoadLayout.setStatus(Status.STATUS_NO_LOADING);
        recycleLoadLayout.setLoadMoreEnable(recycleLoadLayout.getLayoutManager().getItemCount() <= 100);
    }
}
