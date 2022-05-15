package com.tuanche.directselling.dto;

import java.io.Serializable;

/**
 * @author：HuangHao
 * @CreatTime 2020-05-13 15:16
 */
public class GiftRefuelingCardActivityDto implements Serializable {
    private static final long serialVersionUID = 1L;
    //活动是否在进行中 true=进行中 false=已经结束
    private boolean ongoing;
    //活动信息, ongoing=true时才有
    private DealerRefuelCarActivityInfoDto info;
    //进行中或已结束的提示信息
    private String msg;


    public boolean isOngoing() {
        return ongoing;
    }

    public void setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
    }

    public DealerRefuelCarActivityInfoDto getInfo() {
        return info;
    }

    public void setInfo(DealerRefuelCarActivityInfoDto info) {
        this.info = info;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
