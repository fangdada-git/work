package com.tuanche.directselling.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author lvsen
 * @Description 奖励轮播
 * @Date 2021/10/26
 **/
public class AfterSaleRewardSlideshowDto implements Serializable {

    /**
     *
     */
    private Integer id;

    /**
     * 用户类型 1：代理人 2：购买用户 3：分享人
     */
    private Integer userType;

    /**
     * 微信unionid
     */
    private String userWxUnionId;

    /**
     * 奖励类型 1：分享红包 2：购买红包 3：礼品券 4：邀请额外奖励
     */
    private Integer rewardType;

    /**
     * 奖励的单位数量：元或者个等
     */
    private BigDecimal quantity;
    /**
     * 微信昵称
     */
    private String nickName;
    /**
     * 微信头像
     */
    private String userWxHead;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId;
    }

    public Integer getRewardType() {
        return rewardType;
    }

    public void setRewardType(Integer rewardType) {
        this.rewardType = rewardType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
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
}
