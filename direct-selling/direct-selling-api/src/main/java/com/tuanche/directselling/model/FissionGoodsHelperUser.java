package com.tuanche.directselling.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FissionGoodsHelperUser implements Serializable {

    private static final long serialVersionUID = 4889145827127718340L;
    private Integer id;
    //商品id
    private Integer goodsId;
    private String goodsName;
    //裂变活动id
    private Integer fissionId;
    //用户微信openid（被助力）
    private String userWxUnionId;
    private String userWxName;
    private String UserWxHead;
    //助力用户微信openid
    private String helperWxUnionId;
    private String helperWxName;
    private String helperUserWxHead;
    //是否已购买商品   0否  1是
    private Integer buyGoods;
    //创建时间
    private Date createDt;
    //修改时间
    private Date updateDt;
    //是否删除  1：是  0：否
    private Boolean deleteFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
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

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getUserWxName() {
        return userWxName;
    }

    public void setUserWxName(String userWxName) {
        this.userWxName = userWxName;
    }

    public String getUserWxHead() {
        return UserWxHead;
    }

    public void setUserWxHead(String userWxHead) {
        UserWxHead = userWxHead;
    }

    public String getHelperWxName() {
        return helperWxName;
    }

    public void setHelperWxName(String helperWxName) {
        this.helperWxName = helperWxName;
    }

    public String getHelperUserWxHead() {
        return helperUserWxHead;
    }

    public void setHelperUserWxHead(String helperUserWxHead) {
        this.helperUserWxHead = helperUserWxHead;
    }

    public Integer getBuyGoods() {
        return buyGoods;
    }

    public void setBuyGoods(Integer buyGoods) {
        this.buyGoods = buyGoods;
    }

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId;
    }

    public String getHelperWxUnionId() {
        return helperWxUnionId;
    }

    public void setHelperWxUnionId(String helperWxUnionId) {
        this.helperWxUnionId = helperWxUnionId;
    }
}