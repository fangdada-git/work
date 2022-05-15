package com.tuanche.directselling.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tuanche.directselling.model.FashionCarMarkeWinNum;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FashionCarMarkeWinNumResultDto extends FashionCarMarkeWinNum implements Serializable {

    private String cbName;
    private String cbLogo;

    private String cityName;

    private String userName;

    private String userPic;

    private String userPhone;

    private boolean winning;

    private long activityDateMil;

    private String winningNumberStr;

    private long activityDateTime;
    private long nowTime;

    public String getCbName() {
        return cbName;
    }

    public void setCbName(String cbName) {
        this.cbName = cbName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getCbLogo() {
        return cbLogo;
    }

    public void setCbLogo(String cbLogo) {
        this.cbLogo = cbLogo;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public boolean isWinning() {
        return winning;
    }

    public void setWinning(boolean winning) {
        this.winning = winning;
    }

    public long getActivityDateMil() {
        return activityDateMil;
    }

    public void setActivityDateMil(long activityDateMil) {
        this.activityDateMil = activityDateMil;
    }

    public String getWinningNumberStr() {
        return winningNumberStr;
    }

    public void setWinningNumberStr(String winningNumberStr) {
        this.winningNumberStr = winningNumberStr;
    }

    public long getActivityDateTime() {
        return activityDateTime;
    }

    public void setActivityDateTime(long activityDateTime) {
        this.activityDateTime = activityDateTime;
    }

    public long getNowTime() {
        return System.currentTimeMillis();
    }

    public void setNowTime(long nowTime) {
        this.nowTime = nowTime;
    }
}
