package com.kotlin.mvpframe.recycleview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.kotlin.mvpframe.R;

/**
 * Created by Zijin on 2017/7/14.
 */

public class RecycleLoadingMoreView extends RelativeLayout {
    private RecyclerView mRecycleView;
    private View mFooterView;

    private Mode mMode = Mode.NO_LOAD_MORE;

    private boolean isLoading;//是否正在加载

    private boolean pullLoadEnable = true;//是否可以加载更多

    public enum Mode {
        LOADING_MORE,
        NO_LOAD_MORE
    }

    public RecycleLoadingMoreView(@NonNull Context context) {
        this(context, null);
    }

    public RecycleLoadingMoreView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecycleLoadingMoreView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.layout_load, this);

        mRecycleView = findViewById(R.id.recycleView);
        mFooterView = findViewById(R.id.footerview);

        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int mLastVisiblePosition;
            private LinearLayoutManager mLayoutManager;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisiblePosition + 1 == mLayoutManager.getItemCount()) {//显示最后一个

                    if (!isLoading && pullLoadEnable) {
                        changeMode(Mode.LOADING_MORE);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    mLastVisiblePosition = mLayoutManager.findLastVisibleItemPosition();
                }
            }
        });
    }

    public RecyclerView getRecycleView() {
        return mRecycleView;
    }

    public void changeMode(Mode mode) {
        mMode = mode;
        performStatusChange();
    }

    public void setLoadComplete() {
        isLoading = false;
        changeMode(Mode.NO_LOAD_MORE);
    }

    public void setPullLoadEnable(boolean enable) {
        pullLoadEnable = enable;
    }

    /**
     * 判断是否正在加载
     *
     * @return
     */
    public boolean isLoading() {
        return isLoading;
    }

    private static final String TAG = "RecycleLoadingMoreView";

    /**
     * 状态发生改变
     */
    private void performStatusChange() {
        switch (mMode) {
            case LOADING_MORE://正在加载更多
                isLoading = true;
                mFooterView.setVisibility(View.VISIBLE);

                if (mListener != null) {
                    mListener.onLoadMore();
                }

                break;
            case NO_LOAD_MORE://没有更多
                mFooterView.setVisibility(View.GONE);
                break;
        }
    }

    private OnLoadMorelistener mListener;

    /**
     * 设置加载更多监听
     *
     * @param listener
     */
    public void setOnLoadMorelistener(OnLoadMorelistener listener) {
        this.mListener = listener;
    }

    public interface OnLoadMorelistener {
        void onLoadMore();
    }
}
