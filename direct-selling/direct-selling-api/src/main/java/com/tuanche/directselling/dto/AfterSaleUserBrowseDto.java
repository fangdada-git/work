package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.AfterSaleUserBrowse;

import java.io.Serializable;

/**
 * @author：HuangHao
 * @CreatTime 2021-07-21 15:10
 */
public class AfterSaleUserBrowseDto extends AfterSaleUserBrowse implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户微信openid
     */
    private String userWxOpenId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户微信头像
     */
    private String userWxHead;

    public String getUserWxOpenId() {
        return userWxOpenId;
    }

    public void setUserWxOpenId(String userWxOpenId) {
        this.userWxOpenId = userWxOpenId;
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
