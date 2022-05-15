package com.tuanche.directselling.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author：HuangHao
 * @CreatTime 2020-06-03 10:17
 */
public class DealerRefuelCarActivityInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;
    //大场次ID
    private Integer periodsId;
    //大场次名称
    private String periodsName;
    //大场次结束时间
    private Date endTime;
    //大场次结束时间
    private Date beginTime;
    //赠送油卡上限
    private Integer refuelingCardNum;
    //已赠油卡数量
    private Integer presentedCardNum;
    //剩余赠送卡数量
    private Integer surplusCarNum;

    public Integer getSurplusCarNum() {
        if(refuelingCardNum == null || refuelingCardNum < 1 || refuelingCardNum < presentedCardNum){
            return 0;
        }else {
            return refuelingCardNum-(presentedCardNum==null?0:presentedCardNum);
        }
    }

    public Integer getPeriodsId() {
        return periodsId;
    }

    public void setPeriodsId(Integer periodsId) {
        this.periodsId = periodsId;
    }

    public String getPeriodsName() {
        return periodsName;
    }

    public void setPeriodsName(String periodsName) {
        this.periodsName = periodsName;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Integer getRefuelingCardNum() {
        return refuelingCardNum;
    }

    public void setRefuelingCardNum(Integer refuelingCardNum) {
        this.refuelingCardNum = refuelingCardNum;
    }

    public Integer getPresentedCardNum() {
        return presentedCardNum;
    }

    public void setPresentedCardNum(Integer presentedCardNum) {
        this.presentedCardNum = presentedCardNum;
    }
}
