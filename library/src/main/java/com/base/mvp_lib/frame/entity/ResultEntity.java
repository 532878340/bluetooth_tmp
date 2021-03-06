package com.base.mvp_lib.frame.entity;

/**
 * Created by Zijin on 2017/7/12.
 */

public class ResultEntity<T> {
    private String description = "this is description";
    private String code;
    private T data;

    public ResultEntity() {
    }

    /**
     * 判断请求操作是否成功
     */
    public boolean isOk(){
        return "000000".equals(code);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
