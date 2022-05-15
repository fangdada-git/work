package com.tuanche.directselling.vo;


import java.io.Serializable;

/**
 * @author ZhangYiXin
 */
public class CsUserIntegralListVo implements Serializable {

    /**
     * 积分
     */
    private int integral;

    /**
     * 活动PageResult
     */
    private Object pageResult;

    public CsUserIntegralListVo(int integral, Object pageResult) {
        this.integral = integral;
        this.pageResult = pageResult;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public Object getPageResult() {
        return pageResult;
    }

    public void setPageResult(Object pageResult) {
        this.pageResult = pageResult;
    }
}