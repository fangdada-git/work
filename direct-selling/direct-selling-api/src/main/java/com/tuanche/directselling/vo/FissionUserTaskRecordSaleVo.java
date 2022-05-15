package com.tuanche.directselling.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FissionUserTaskRecordSaleVo implements Serializable {
    /**
     *
     */
    private String id;
    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户微信头像
     */
    private String userWxHead;

    /**
     * 时间描述
     */
    private String dateDesc;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDt;

    /**
     * 积分
     */
    private BigDecimal integral;

    /**
     * 是否观看直播
     */
    private Boolean isLive;

    /**
     * 是否预约直播
     */
    private Boolean isSubscribe;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserWxHead() {
        return userWxHead;
    }

    public void setUserWxHead(String userWxHead) {
        this.userWxHead = userWxHead;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public BigDecimal getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    public String getDateDesc() {
        return dateDesc;
    }

    public void setDateDesc(String dateDesc) {
        this.dateDesc = dateDesc;
    }

    public Boolean getLive() {
        return isLive;
    }

    public void setLive(Boolean live) {
        isLive = live;
    }

    public Boolean getSubscribe() {
        return isSubscribe;
    }

    public void setSubscribe(Boolean subscribe) {
        isSubscribe = subscribe;
    }

}
