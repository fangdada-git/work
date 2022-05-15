package com.tuanche.web.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/7/19 18:04
 **/
public class AfterSaleAgentRegister {
    /**
     * 代理人微信unionid
     */
    @NotNull
    private String agentWxUnionId;


    /**
     * 代理人微信openId
     */
    @NotNull
    private String agentWxOpenId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 微信头像
     */
    private String wxHead;

    /**
     * 姓名
     */
    @NotNull
    private String name;

    /**
     * 电话
     */
    @NotNull
    @Pattern(regexp = "^(13[0-9]|14[5|7]|15[0-9]|16[0-9]|18[0-9]|17[0-9]|19[0-9])\\d{8}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 职位
     */
    @NotNull
    private String position;

    /**
     * 活动ID
     */
    private Integer activityId;

    /**
     * 经销商ID
     */
    private Integer dealerId;

    /**
     * 代理人类型 0销售  1团车电销
     */
    @NotNull
    private Integer type;

    public String getAgentWxUnionId() {
        return agentWxUnionId;
    }

    public void setAgentWxUnionId(String agentWxUnionId) {
        this.agentWxUnionId = agentWxUnionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public String getAgentWxOpenId() {
        return agentWxOpenId;
    }

    public void setAgentWxOpenId(String agentWxOpenId) {
        this.agentWxOpenId = agentWxOpenId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getWxHead() {
        return wxHead;
    }

    public void setWxHead(String wxHead) {
        this.wxHead = wxHead;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
