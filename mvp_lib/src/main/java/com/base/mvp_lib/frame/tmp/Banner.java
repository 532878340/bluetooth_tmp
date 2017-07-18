package com.base.mvp_lib.frame.tmp;

/**
 * Created by Zijin on 2017/7/12.
 */

public class Banner {
    private String contentUrl;
    private String contentTitle;

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    @Override
    public String toString() {
        return "Banner{" +
                "contentUrl='" + contentUrl + '\'' +
                ", contentTitle='" + contentTitle + '\'' +
                '}';
    }
}
