package com.tuanche.directselling.vo;


import java.io.Serializable;

/**
 * @author ZhangYiXin
 */
public class CsUserActivityVo implements Serializable {

    /**
     * 姓名
     */
    private String uname;

    /**
     * 活动PageResult
     */
    private Object pageResult;

    public CsUserActivityVo(String uname, Object pageResult) {
        this.uname = uname;
        this.pageResult = pageResult;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Object getPageResult() {
        return pageResult;
    }

    public void setPageResult(Object pageResult) {
        this.pageResult = pageResult;
    }
}