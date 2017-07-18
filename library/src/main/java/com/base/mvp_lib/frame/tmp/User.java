package com.base.mvp_lib.frame.tmp;

import java.util.List;

/**
 * Created by Zijin on 2017/7/12.
 */

public class User {
    private List<Banner> bannerList;

    public List<Banner> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<Banner> bannerList) {
        this.bannerList = bannerList;
    }

    @Override
    public String toString() {
        return "User{" +
                "bannerList=" + bannerList +
                '}';
    }
}
