package com.base.mvp_lib.frame.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.base.mvp_lib.R;
import com.base.mvp_lib.frame.contracts.IBaseContracts;
import com.base.mvp_lib.frame.presenter.BasePresenter;

/**
 * Created by Zijin on 2017/7/12.
 */

public abstract class BaseActivity<P extends BasePresenter, V extends IBaseContracts.IBaseView> extends FrameRootActivity<P, V> implements IBaseContracts.IBaseView {
    private static final String TAG = "BaseActivity";

    protected Context mCtx;

    protected ProgressDialog mProgressDialog;//加载进度条

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_base_act);
        mCtx = this;
    }

    @Override
    public void initLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mCtx);
        }
        mProgressDialog.show();
    }

    @Override
    public void showLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mCtx);
        }
        mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void showToastMsg(String message) {
        Toast.makeText(mCtx, message, Toast.LENGTH_SHORT).show();
    }

    /************************** 生命周期相关 ***************************/

    @Override
    protected void onRestart() {
        super.onRestart();
        mPresenter.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
    }
}
