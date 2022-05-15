package com.tuanche.directselling.utils;

import java.io.Serializable;
import java.util.List;

/**
 * @Author gongbo
 * @Description
 * @Date 2020年3月4日16:55:42
 **/
public class PageResult<T> implements Serializable {
    /**
     * home page
     */
    private int page=1;

    /**
     * size of each page
     */
    private int limit=10;

    /**
     * total data quantity
     */
    private long count;

    private long pageCount;

    /**
     * 总量
     */
    private long pageTotal;

    /**
     * status code
     */
    private String code;

    /**
     * describe message
     */
    private String msg;

    /**
     * returned data
     */
    private List<T> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCode(int code) {
        this.code = String.valueOf(code);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getPageCount() {
        return count%limit>0?count/limit+1 : count/limit;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public long getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(long pageTotal) {
        this.pageTotal = pageTotal;
    }
}