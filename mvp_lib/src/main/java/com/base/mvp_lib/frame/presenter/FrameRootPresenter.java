package com.base.mvp_lib.frame.presenter;

import com.base.mvp_lib.frame.contracts.IContracts;

import java.lang.ref.WeakReference;

/**
 * Created by Zijin on 2017/7/12.
 */

public abstract class FrameRootPresenter<V extends IContracts.IView,M extends IContracts.IModel> {
    private WeakReference<V> mWeakReference;
    protected M mModel;

    public void attach(V v){
        mWeakReference = new WeakReference<>(v);
        mModel = getModel();
    }

    protected abstract M getModel();

    public void detach(){
        if(mWeakReference != null){
            mWeakReference.clear();
            mWeakReference = null;
        }
    }

    public V getView(){
        if(mWeakReference != null){
            return mWeakReference.get();
        }

        return null;
    }

    public boolean isViewAttach(){
        return mWeakReference != null & mWeakReference.get() != null;
    }
}
