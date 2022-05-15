package com.tuanche.directselling.pojo;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/10/15 17:55
 **/
public class PlwDto {
    private String lotteryDrawNum;
    private String lotteryDrawResult;
    private String lotteryDrawTime;

    public String getLotteryDrawNum() {
        return lotteryDrawNum;
    }

    public void setLotteryDrawNum(String lotteryDrawNum) {
        this.lotteryDrawNum = lotteryDrawNum;
    }

    public String getLotteryDrawResult() {
        return lotteryDrawResult == null ? null : lotteryDrawResult.replaceAll(" ", "");
    }

    public void setLotteryDrawResult(String lotteryDrawResult) {
        this.lotteryDrawResult = lotteryDrawResult;
    }

    public String getLotteryDrawTime() {
        return lotteryDrawTime;
    }

    public void setLotteryDrawTime(String lotteryDrawTime) {
        this.lotteryDrawTime = lotteryDrawTime;
    }
}
