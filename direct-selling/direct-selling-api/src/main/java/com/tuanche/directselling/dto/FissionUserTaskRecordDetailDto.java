package com.tuanche.directselling.dto;

import java.io.Serializable;
import java.util.Date;

public class FissionUserTaskRecordDetailDto implements Serializable {
    /**
     *
     */
    private Long id;
    /**
     * 用户微信UnionId
     */
    private String userWxUnionId;
    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户微信头像
     */
    private String userWxHead;

    /**
     * 销售名称
     */
    private String saleName;

    /**
     * 经销商id
     */
    private Integer dealerId;

    /**
     * 0：未开始或未开启数据无积分数据 1：已开启无积分数据 2：已开启有积分数
     */
    private Integer ongoingData;

    /**
     * 渠道
     */
    private Integer channel;
    /**
     * 创建时间
     */
    private Date createDt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Integer getOngoingData() {
        return ongoingData;
    }

    public void setOngoingData(Integer ongoingData) {
        this.ongoingData = ongoingData;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }
}
