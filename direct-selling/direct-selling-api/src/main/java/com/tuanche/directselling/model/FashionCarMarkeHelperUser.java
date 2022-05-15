package com.tuanche.directselling.model;

import com.tuanche.commons.util.StringUtil;

import java.io.Serializable;
import java.util.Date;

public class FashionCarMarkeHelperUser implements Serializable {
    private Integer id;
    //大场次id
    private Integer periodsId;
    //潮车集id
    private Integer carFashionId;
    //goods_id
    private Integer goodsId;
    //类型 1：半价车  2：抵扣券  3：现金红包
    private Integer helperType;
    //被助力用户id
    private Integer userId;
    //被助力用户微信unionid
    private String userWxUnionId;
    //被助力用户微信昵称
    private String userWxNick;
    //被助力用户微信头像
    private String userWxHead;
    //助力用户id
    private Integer helperUserId;
    //助力用户微信unionid
    private String helperWxUnionId;
    //助力用户微信昵称
    private String helperWxNick;
    //助力用户微信头像
    private String helperWxHead;
    //是否已购买商品   0否  1是
    private Integer buyFlag;
    //中奖码发放标识  1发放  2未发放
    private Integer grantWinNumFlag;

    private Date createDt;

    private Date updateDt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCarFashionId() {
        return carFashionId;
    }

    public void setCarFashionId(Integer carFashionId) {
        this.carFashionId = carFashionId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getHelperType() {
        return helperType;
    }

    public void setHelperType(Integer helperType) {
        this.helperType = helperType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = StringUtil.isEmpty(userWxUnionId) ? null : userWxUnionId.trim();
    }

    public String getUserWxNick() {
        return userWxNick;
    }

    public void setUserWxNick(String userWxNick) {
        this.userWxNick = StringUtil.isEmpty(userWxNick) ? null : userWxNick.trim();
    }

    public String getUserWxHead() {
        return userWxHead;
    }

    public void setUserWxHead(String userWxHead) {
        this.userWxHead = StringUtil.isEmpty(userWxHead) ? null : userWxHead.trim();
    }

    public Integer getHelperUserId() {
        return helperUserId;
    }

    public void setHelperUserId(Integer helperUserId) {
        this.helperUserId = helperUserId;
    }

    public String getHelperWxUnionId() {
        return helperWxUnionId;
    }

    public void setHelperWxUnionId(String helperWxUnionId) {
        this.helperWxUnionId = StringUtil.isEmpty(helperWxUnionId) ? null : helperWxUnionId.trim();
    }

    public String getHelperWxNick() {
        return helperWxNick;
    }

    public void setHelperWxNick(String helperWxNick) {
        this.helperWxNick = StringUtil.isEmpty(helperWxNick) ? null : helperWxNick.trim();
    }

    public String getHelperWxHead() {
        return helperWxHead;
    }

    public void setHelperWxHead(String helperWxHead) {
        this.helperWxHead = StringUtil.isEmpty(helperWxHead) ? null : helperWxHead.trim();
    }

    public Integer getBuyFlag() {
        return buyFlag;
    }

    public void setBuyFlag(Integer buyFlag) {
        this.buyFlag = buyFlag;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public Integer getPeriodsId() {
        return periodsId;
    }

    public void setPeriodsId(Integer periodsId) {
        this.periodsId = periodsId;
    }

    public Integer getGrantWinNumFlag() {
        return grantWinNumFlag;
    }

    public void setGrantWinNumFlag(Integer grantWinNumFlag) {
        this.grantWinNumFlag = grantWinNumFlag;
    }
}