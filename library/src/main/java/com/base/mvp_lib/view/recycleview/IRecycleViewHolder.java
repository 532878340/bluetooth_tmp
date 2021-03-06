package com.base.mvp_lib.view.recycleview;

import android.support.annotation.IdRes;
import android.view.View;

/**
 * Created by Zijin on 2017/7/13.
 */

public interface IRecycleViewHolder {
    /**
     * 根据id返回指定view
     */
    <V extends View> V getView(@IdRes int id);
}
