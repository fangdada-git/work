package com.tuanche.directselling.vo;/**
 * @program: direct-selling
 * @description: ${description}
 * @author: lvsen
 * @create: 2020-10-09 16:36
 **/

import java.io.Serializable;

/**
 * @Author lvsen
 * @Description 经销商销售
 * @Date 2020/10/9
 **/
public class DealerSalesVo implements Serializable {
    private static final long serialVersionUID = -5902499897972399243L;
    /**
     * 销售手机号
     */
    private String uphone;
    /**
     * 销售名称
     */
    private String uname;
    /**
     * 级别
     */
    private Integer ulevel;
    /**
     * 是否参与
     */
    private boolean joinFission;

    public String getUphone() {
        return uphone;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Integer getUlevel() {
        return ulevel;
    }

    public void setUlevel(Integer ulevel) {
        this.ulevel = ulevel;
    }

    public boolean isJoinFission() {
        return joinFission;
    }

    public void setJoinFission(boolean joinFission) {
        this.joinFission = joinFission;
    }
}
