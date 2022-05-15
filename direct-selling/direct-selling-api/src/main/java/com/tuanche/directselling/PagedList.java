package com.tuanche.directselling;


import java.util.List;

/**
 * 分页通用返回数据
 *
 * @param <T>
 */
public class PagedList<T> {

    private long pageNo = 1;

    private long pageSize = 20;

    private long totalRows = 0;

    private List<T> data;

    public long getTotalPages() {
        if (pageSize == 0) {
            return 0;
        }
        return (long) Math.ceil(totalRows * 1.0D / pageSize);
    }

    public long getPageNo() {
        return pageNo;
    }

    public void setPageNo(long pageNo) {
        this.pageNo = pageNo;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(long totalRows) {
        this.totalRows = totalRows;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }


}
