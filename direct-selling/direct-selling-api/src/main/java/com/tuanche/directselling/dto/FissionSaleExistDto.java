package com.tuanche.directselling.dto;

import java.io.Serializable;

/**
 * @author：HuangHao
 * @CreatTime 2020-10-14 11:33
 */
public class FissionSaleExistDto implements Serializable {
    private static final long serialVersionUID = 1L;
    //分享人微信UnionId
    private String shareWxUnionId;
    //用户微信UnionId
    private String userWxUnionId;
    //完成任务的用户是否是销售：0=否 1=是
    private int userIsSale;
    //分享人用户是否是销售：0=否 1=是
    private int shareIsSale;
    //裂变活动ID
    private Integer fissionId;


    public String getShareWxUnionId() {
        return shareWxUnionId;
    }

    public void setShareWxUnionId(String shareWxUnionId) {
        this.shareWxUnionId = shareWxUnionId;
    }

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId;
    }

    public int getUserIsSale() {
        return userIsSale;
    }

    public void setUserIsSale(int userIsSale) {
        this.userIsSale = userIsSale;
    }

    public int getShareIsSale() {
        return shareIsSale;
    }

    public void setShareIsSale(int shareIsSale) {
        this.shareIsSale = shareIsSale;
    }

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
    }
}
