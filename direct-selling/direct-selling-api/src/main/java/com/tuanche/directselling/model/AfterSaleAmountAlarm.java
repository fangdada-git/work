package com.tuanche.directselling.model;

import com.tuanche.commons.util.StringUtil;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AfterSaleAmountAlarm implements Serializable {
    private Integer id;

    //累计充值金额
    private BigDecimal accruingAmounts=new BigDecimal(0);

    //已裂变发放金额
    private BigDecimal paymentAmount=new BigDecimal(0);
    //余额
    private BigDecimal amount;

    //报警余额
    private BigDecimal alarmAmount;

    //报警手机号,分隔
    private String alarmPhone;
    private List<String> alarmPhoneList;

    //报警邮箱,分隔
    private String alarmEmail;
    private List<String> alarmEmailList;

    private Date updateDt;

    //充值金额
    private BigDecimal rechargeAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAccruingAmounts() {
        return accruingAmounts;
    }

    public void setAccruingAmounts(BigDecimal accruingAmounts) {
        this.accruingAmounts = accruingAmounts;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getAlarmAmount() {
        return alarmAmount;
    }

    public void setAlarmAmount(BigDecimal alarmAmount) {
        this.alarmAmount = alarmAmount;
    }

    public String getAlarmPhone() {
        return alarmPhone;
    }

    public void setAlarmPhone(String alarmPhone) {
        this.alarmPhone = alarmPhone == null ? null : alarmPhone.trim();
    }

    public String getAlarmEmail() {
        return alarmEmail;
    }

    public void setAlarmEmail(String alarmEmail) {
        this.alarmEmail = alarmEmail == null ? null : alarmEmail.trim();
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public List<String> getAlarmPhoneList() {
        if (!StringUtil.isEmpty(alarmPhone)) {
            alarmPhoneList = Arrays.asList(alarmPhone.split(",", 100));
        } else {
            alarmPhoneList = new ArrayList<>();
            alarmPhoneList.add(" ");
        }
        return alarmPhoneList;
    }

    public void setAlarmPhoneList(List<String> alarmPhoneList) {
        this.alarmPhoneList = alarmPhoneList;
    }

    public List<String> getAlarmEmailList() {
        if (!StringUtil.isEmpty(alarmEmail)) {
            alarmEmailList = Arrays.asList(alarmEmail.split(",", 100));
        }else {
            alarmEmailList = new ArrayList<>();
            alarmEmailList.add("");
        }
        return alarmEmailList;
    }

    public void setAlarmEmailList(List<String> alarmEmailList) {
        this.alarmEmailList = alarmEmailList;
    }

    public BigDecimal getAmount() {
        return amount = accruingAmounts.subtract(paymentAmount);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(BigDecimal rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }
}