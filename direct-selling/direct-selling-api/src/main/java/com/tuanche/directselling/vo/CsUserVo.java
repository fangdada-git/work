package com.tuanche.directselling.vo;

import java.io.Serializable;

/**
 * @author ZhangYiXin
 */
public class CsUserVo implements Serializable {

    /**
     * 表 : cs_user
     * 对应字段 : id
     */
    private Integer id;

    /**
     * 姓名
     * 表 : cs_user
     * 对应字段 : uname
     */
    private String uname;

    /**
     * 角色：0：厂商管理员；1：大区管理员；2：小区管理员；3：经销商管理员；4：销售顾问;
     * 表 : cs_user
     * 对应字段 : ulevel
     */
    private String ulevel;

    /**
     * 经销商名称
     */
    private String dealerName;

    /**
     * 积分
     */
    private Integer integral;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUlevel() {
        return ulevel;
    }

    public void setUlevel(String ulevel) {
        this.ulevel = ulevel;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral == null ? 0 : integral;
    }
}