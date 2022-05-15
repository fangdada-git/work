package com.tuanche.directselling.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tuanche.directselling.model.FashionCarMarkeHelperUser;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FashionCarMarkeHelperUserDto extends FashionCarMarkeHelperUser {

    //用户助力标记 0：未助力  1：已助力该用户商品  2：助力其他用户商品
    private  Integer helperFlag=0;
    /**
     *  助力人数
     */
    private int helperCount;

    public Integer getHelperFlag() {
        return helperFlag;
    }

    public void setHelperFlag(Integer helperFlag) {
        this.helperFlag = helperFlag;
    }

    public int getHelperCount() {
        return helperCount;
    }

    public void setHelperCount(int helperCount) {
        this.helperCount = helperCount;
    }
}
