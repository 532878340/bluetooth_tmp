package com.base.mvp_lib.frame.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.base.mvp_lib.frame.contracts.IBaseContracts;
import com.base.mvp_lib.frame.presenter.BasePresenter;

/**
 * Created by Zijin on 2017/7/17.
 * Email:info@zijinqianbao.com
 */

public abstract class BaseFragment<P extends BasePresenter, V extends IBaseContracts.IBaseView> extends FrameRootFragment<P, V> implements IBaseContracts.IBaseView {
    protected Context mCtx;

    protected ProgressDialog mProgressDialog;//加载进度条

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCtx = context;
    }

    @Override
    public void initLoading() {
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(mCtx);
        }
        mProgressDialog.show();
    }

    @Override
    public void showLoading() {
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(mCtx);
        }
        mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(mCtx);
        }
        mProgressDialog.show();
    }

    @Override
    public void showError() {

    }

    @Override
    public void showToastMsg(String message) {
        Toast.makeText(mCtx, message, Toast.LENGTH_SHORT).show();
    }
}
