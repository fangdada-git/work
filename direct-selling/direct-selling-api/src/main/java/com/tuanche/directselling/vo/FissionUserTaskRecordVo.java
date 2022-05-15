package com.tuanche.directselling.vo;

import com.tuanche.directselling.model.FissionUserTaskRecord;

/**
 * @author：HuangHao
 * @CreatTime 2020-09-23 16:00
 */
public class FissionUserTaskRecordVo extends FissionUserTaskRecord {

    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 城市id
     */
    private Integer cityId;
    /**
     * 用户微信WeiXinId
     */
    private String userWxOpenId;
    //微信用户头像
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

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getUserWxHead() {
        return userWxHead;
    }

    public void setUserWxHead(String userWxHead) {
        this.userWxHead = userWxHead;
    }
}
