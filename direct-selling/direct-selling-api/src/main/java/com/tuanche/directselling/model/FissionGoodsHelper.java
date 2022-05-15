package com.tuanche.directselling.model;

import java.io.Serializable;

public class FissionGoodsHelper implements Serializable {
    private static final long serialVersionUID = -7265912456814378903L;
    
    private Integer id;
    //商品id
    private Integer goodsId;
    //场次id
    private Integer peroidsId;
    //裂变活动id
    private Integer fissionId;
    //是否需要助力 1是  2否
    private Integer flag;
    //助力人数
    private Integer helperNum;
    //删除状态  1：是  0：否
    private Integer deleteFlag;

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

    public Integer getPeroidsId() {
        return peroidsId;
    }

    public void setPeroidsId(Integer peroidsId) {
        this.peroidsId = peroidsId;
    }

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getHelperNum() {
        return helperNum;
    }

    public void setHelperNum(Integer helperNum) {
        this.helperNum = helperNum;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}