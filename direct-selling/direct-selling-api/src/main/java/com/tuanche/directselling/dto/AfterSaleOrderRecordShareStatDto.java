package com.tuanche.directselling.dto;

import java.io.Serializable;

/**
 * 分享统计数据
 * @author：HuangHao
 * @CreatTime 2021-08-18 15:00
 */
public class AfterSaleOrderRecordShareStatDto implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 活动ID
     */
    private Integer activityId;

    //分享人微信unionid
    private String shareWxUnionId;
    //邀请人数
    public Integer inviteesNum;

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getShareWxUnionId() {
        return shareWxUnionId;
    }

    public void setShareWxUnionId(String shareWxUnionId) {
        this.shareWxUnionId = shareWxUnionId;
    }

    public Integer getInviteesNum() {
        return inviteesNum;
    }

    public void setInviteesNum(Integer inviteesNum) {
        this.inviteesNum = inviteesNum;
    }
}
