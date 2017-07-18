package com.base.mvp_lib.frame.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.base.mvp_lib.frame.presenter.FrameRootPresenter;
import com.base.mvp_lib.frame.contracts.IContracts;

/**
 * Created by Zijin on 2017/7/12.
 */

public abstract class FrameRootActivity<P extends FrameRootPresenter, V extends IContracts.IView> extends AppCompatActivity {
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = getPresenter();

        if(mPresenter != null){
            mPresenter.attach((V) this);
        }
    }

    @NonNull
    protected abstract P getPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.detach();
        }
    }
}
