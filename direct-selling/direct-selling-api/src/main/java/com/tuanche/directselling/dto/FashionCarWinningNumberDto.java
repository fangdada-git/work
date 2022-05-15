package com.tuanche.directselling.dto;

import com.tuanche.directselling.utils.GlobalConstants;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/9/3 13:30
 **/
public class FashionCarWinningNumberDto implements Serializable {
    private Integer id;
    private Integer winNum;
    private String winNumStr="";
    private Long createDt;
    /**
     * 是否中奖
     */
    private Boolean winFlag;
    /**
     *  是否参与
     */
    private Boolean takeFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWinNum() {
        return winNum;
    }

    public void setWinNum(Integer winNum) {
        this.winNum = winNum;
    }

    public Long getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Long createDt) {
        this.createDt = createDt;
    }

    public String getWinNumStr() {
        return GlobalConstants.StringFormat(8, this.winNum);
    }

    public void setWinNumStr(String winNumStr) {
        this.winNumStr = winNumStr;
    }

    public Boolean getWinFlag() {
        return winFlag;
    }

    public void setWinFlag(Boolean winFlag) {
        this.winFlag = winFlag;
    }

    public Boolean getTakeFlag() {
        return takeFlag;
    }

    public void setTakeFlag(Boolean takeFlag) {
        this.takeFlag = takeFlag;
    }
}
