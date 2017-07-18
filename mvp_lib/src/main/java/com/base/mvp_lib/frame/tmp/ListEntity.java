package com.base.mvp_lib.frame.tmp;

import java.util.List;

/**
 * Created by Zijin on 2017/7/14.
 * Email:info@zijinqianbao.com
 */

public class ListEntity {
    private int recordCount;
    private boolean hasNextPage;
    private int pageTotal;
    private int pageSize;
    private List<SteadyEntity> list;

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<SteadyEntity> getList() {
        return list;
    }

    public void setList(List<SteadyEntity> list) {
        this.list = list;
    }
}
