package com.tuanche.directselling.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * fission_sale
 *
 * @author
 */
public class FissionUserRewardRankVo implements Serializable {
    private Integer rank;
    /**
     * 昵称
     */
    private String nickName;

    /**
     * 邀请用户数
     */
    private Integer inviteUserCount;

    /**
     * 邀请预约数
     */
    private Integer inviteSubscribeCount;

    /**
     * 邀请用户购买数
     */
    private Integer inviteUserBuyCount;

    /**
     * 实际收入金额
     */
    private BigDecimal realIncome;


    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getInviteUserCount() {
        return inviteUserCount;
    }

    public void setInviteUserCount(Integer inviteUserCount) {
        this.inviteUserCount = inviteUserCount;
    }

    public Integer getInviteSubscribeCount() {
        return inviteSubscribeCount;
    }

    public void setInviteSubscribeCount(Integer inviteSubscribeCount) {
        this.inviteSubscribeCount = inviteSubscribeCount;
    }

    public Integer getInviteUserBuyCount() {
        return inviteUserBuyCount;
    }

    public void setInviteUserBuyCount(Integer inviteUserBuyCount) {
        this.inviteUserBuyCount = inviteUserBuyCount;
    }

    public BigDecimal getRealIncome() {
        return realIncome;
    }

    public void setRealIncome(BigDecimal realIncome) {
        this.realIncome = realIncome;
    }
}