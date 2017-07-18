package com.base.mvp_lib.frame.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.base.mvp_lib.frame.contracts.IContracts;
import com.base.mvp_lib.frame.presenter.FrameRootPresenter;

/**
 * Created by Zijin on 2017/7/17.
 * Email:info@zijinqianbao.com
 */

public abstract class FrameRootFragment<P extends FrameRootPresenter, V extends IContracts.IView> extends Fragment{
    protected P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = getPresenter();
        if(mPresenter != null){
            mPresenter.attach((V) this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.detach();
        }
    }

    @NonNull
    protected abstract P getPresenter();
}
