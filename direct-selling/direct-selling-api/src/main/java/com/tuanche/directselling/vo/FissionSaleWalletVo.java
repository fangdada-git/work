package com.tuanche.directselling.vo;

import com.tuanche.directselling.utils.PageResult;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2020/9/23 18:03
 **/
public class FissionSaleWalletVo implements Serializable {

    /**
     * 等待提现金额
     */
    private BigDecimal waitingWithdrawal;
    /**
     * 已提现金额
     */
    private BigDecimal withdrawaled;
    /**
     * 提现列表
     */
    private PageResult withdrawalList;

    public BigDecimal getWaitingWithdrawal() {
        return waitingWithdrawal;
    }

    public void setWaitingWithdrawal(BigDecimal waitingWithdrawal) {
        this.waitingWithdrawal = waitingWithdrawal;
    }

    public BigDecimal getWithdrawaled() {
        return withdrawaled;
    }

    public void setWithdrawaled(BigDecimal withdrawaled) {
        this.withdrawaled = withdrawaled;
    }

    public PageResult getWithdrawalList() {
        return withdrawalList;
    }

    public void setWithdrawalList(PageResult withdrawalList) {
        this.withdrawalList = withdrawalList;
    }
}
