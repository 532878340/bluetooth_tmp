package com.kotlin.mvpframe.recycleview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Zijin on 2017/7/14.
 */

public class RecycleLoadingView extends RecyclerView {
    LinearLayoutManager mLayoutManager;

    private int mLastVisibleItem;

    public RecycleLoadingView(Context context) {
        this(context, null);
    }

    public RecycleLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecycleLoadingView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == SCROLL_STATE_IDLE && mLastVisibleItem + 1 == mLayoutManager.getItemCount()) {
                    //显示到底部

                    //设置刷新状态,请求网络数据
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mLayoutManager != null) {
                    mLastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                }
            }
        });
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);

        if (layout instanceof LinearLayoutManager) {
            mLayoutManager = (LinearLayoutManager) layout;
        }
    }
}
