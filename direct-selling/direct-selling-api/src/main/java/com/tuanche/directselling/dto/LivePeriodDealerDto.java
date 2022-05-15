package com.tuanche.directselling.dto;

import java.io.Serializable;

/**
 * @class: LivePeriodDealerDto
 * @description: 直卖场次经销商信息实体
 * @author: dudg
 * @create: 2020-06-23 17:28
 */
public class LivePeriodDealerDto implements Serializable {

    /**
     * 场次id
     */
    private Integer periodsId;
    /**
     * 场次名称
     */
    private String periodsName;
    /**
     * 场次活动id
     */
    private Integer sceneId;
    /**
     * 场次活动标题
     */
    private String sceneTitle;
    /**
     * 城市id
     */
    private Integer cityId;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 经销商id
     */
    private Integer dealerId;
    /**
     * 经销商名称
     */
    private String dealerName;
    /**
     * 合作经销商名称
     */
    private String contractDealerName;
    /**
     * 直播品牌
     */
    private String brandNames;

    /**
     * 负责人姓名
     */
    private String managerName;
    /**
     * 负责人手机号
     */
    private String managerPhone;

    /**
     * 参与裂变
     */
    private boolean joinFission;

    /**
     * 是否分页
     */
    private boolean openPage;

    //经销商结算账户数据ID
    private Integer dealerSttlementAccountId;
    //经销商结算账户数据ID
    private Integer fissionId;
    //是否添加了结算账户 0：否 1：是
    private Integer hasSttlementAccount;
    //账号类型 1：对公账户 2：个人账户
    private Integer accountType;
    //经销商财务信息表ID
    private Integer dealerFinancialInvoiceId;

    private Boolean paymentStatus;

    private String bankCardName;

    private String bankCardNumber;

    private String bankName;

    private String bankAddress;

    private String dutyParagraph;
    /**
     * 基础数据和业务数据是否一致 0:不一致 1:一致
     */
    private Integer syncStatus;

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

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public String getSceneTitle() {
        return sceneTitle;
    }

    public void setSceneTitle(String sceneTitle) {
        this.sceneTitle = sceneTitle;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getContractDealerName() {
        return contractDealerName;
    }

    public void setContractDealerName(String contractDealerName) {
        this.contractDealerName = contractDealerName;
    }

    public String getBrandNames() {
        return brandNames;
    }

    public void setBrandNames(String brandNames) {
        this.brandNames = brandNames;
    }

    public boolean getOpenPage() {
        return openPage;
    }

    public void setOpenPage(boolean openPage) {
        this.openPage = openPage;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public boolean isJoinFission() {
        return joinFission;
    }

    public void setJoinFission(boolean joinFission) {
        this.joinFission = joinFission;
    }

    public Integer getDealerSttlementAccountId() {
        return dealerSttlementAccountId;
    }

    public void setDealerSttlementAccountId(Integer dealerSttlementAccountId) {
        this.dealerSttlementAccountId = dealerSttlementAccountId;
    }

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
    }

    public Integer getHasSttlementAccount() {
        return hasSttlementAccount;
    }

    public void setHasSttlementAccount(Integer hasSttlementAccount) {
        this.hasSttlementAccount = hasSttlementAccount;
    }

    public Boolean getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public Integer getDealerFinancialInvoiceId() {
        return dealerFinancialInvoiceId;
    }

    public void setDealerFinancialInvoiceId(Integer dealerFinancialInvoiceId) {
        this.dealerFinancialInvoiceId = dealerFinancialInvoiceId;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public Integer getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(Integer syncStatus) {
        this.syncStatus = syncStatus;
    }

    public boolean isOpenPage() {
        return openPage;
    }

    public String getBankCardName() {
        return bankCardName;
    }

    public void setBankCardName(String bankCardName) {
        this.bankCardName = bankCardName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getDutyParagraph() {
        return dutyParagraph;
    }

    public void setDutyParagraph(String dutyParagraph) {
        this.dutyParagraph = dutyParagraph;
    }
}
