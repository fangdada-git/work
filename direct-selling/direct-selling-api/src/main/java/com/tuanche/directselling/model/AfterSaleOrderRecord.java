package com.tuanche.directselling.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tuanche.commons.util.StringUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AfterSaleOrderRecord implements Serializable {
    private Integer id;

    //订单编号
    private String orderCode;

    //售后特卖活动id
    private Integer activityId;

    //经销商id
    private Integer dealerId;

    //商品id
    private Integer goodsId;

    //订单价格
    private BigDecimal orderMoney;

    //订单状态:  1:待支付 2:支付中 3:已支付 4:核销 5:申请退款 6:退款中 7:退款完成 8:等待买家确认收货 11待发券 12已进店
    private Integer orderStatus;
    private List<Integer> orderStatusList;
    //订单类型 1：推广卡 2：套餐卡
    private Integer orderType;

    //用户姓名
    private Integer userId;
    private String userName;

    //用户手机号
    private String userPhone;

    //城市id
    private Integer cityId;

    //车牌
    private String licencePlate;

    //代理人微信unionid
    private String agentWxUnionId;

    //分享人微信unionid
    private String shareWxUnionId;

    //用户微信unionid
    private String userWxUnionId;

    //用户微信openid
    private String userWxOpenId;

    private Integer cbId;
    private Integer csId;

    //当前页面的URL（取自Referer）
    private String pageUrl;

    //ip地址
    private String ip;

    //推广渠道 1：代理人 2：电销客服 3：裸连接（公众号）
    private Integer channel;

    //是否为备案用户 0：不是 1：是
    private Boolean keepOnRecordUser;
    //用户类型 0：备案用户 1：流失用户 2：普通用户
    private Integer userType;

    //是否完成了裂变任务 0：未完成 1：完成
    private Boolean finishFissionTask;

    //购买人的裂变层级，超出2级的购买不给代理人奖励
    private Integer fissionLevel;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    /** 团车的transaction_id，对应收银台的seriesNo或trade_id */
    private String tcTransactionId;

    /** 微信支付订单号（交易流水号） */
    private String wxTransactionId;
    /**
     * 分账状态 1：不分账 2：待分账 3：分账中 4：已分账 5：分账失败 6：分账退回中 7：分账已退回 8：分账退回失败
     */
    private Integer subAccountStatus;

    /**
     * 分账金额
     */
    private BigDecimal subAccountAmount;

    /**
     * 分账时间
     */
    private Date subAccountTime;

    /**
     * 微信分账明细单号，每笔分账业务执行的明细单号，可与资金账单对账使用
     */
    private String subAccountTransactionDetailId;

    /**
     * 微信分账接口返回码
     */
    private String subAccountResultCode;

    /**
     * 分账/退回结果，枚举值：PENDING：待分账，SUCCESS：分账成功，CLOSED：分账失败已关闭，PROCESSING:处理中，FAILED：失败
     */
    private String subAccountReceiverResult;

    /**
     * 分账状态/失败原因描述
     */
    private String subAccountDesc;

    /**
     * 微信分账回退单号，微信支付系统返回的唯一标识
     */
    private String subAccountReturnNo;

    /**
     * 分账退回时间
     */
    private Date subAccountReturnTime;
    //核销销售id
    private Integer checkedSalesId;
    //核销经销商id
    private Integer checkedDealerId;
    //核销时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date checkedDate;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDt;

    //修改时间
    private Date updateDt;

    //是否删除 0未删除 1 删除
    private Integer deleteFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = StringUtil.isEmpty(orderCode) ? null : orderCode.trim();
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = StringUtil.isEmpty(userName) ? null : userName.trim();
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = StringUtil.isEmpty(userPhone) ? null : userPhone.trim();
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = StringUtil.isEmpty(licencePlate) ? null : licencePlate.trim();
    }

    public String getAgentWxUnionId() {
        return agentWxUnionId;
    }

    public void setAgentWxUnionId(String agentWxUnionId) {
        this.agentWxUnionId = StringUtil.isEmpty(agentWxUnionId) ? null : agentWxUnionId.trim();
    }

    public String getShareWxUnionId() {
        return shareWxUnionId;
    }

    public void setShareWxUnionId(String shareWxUnionId) {
        this.shareWxUnionId =  StringUtil.isEmpty(shareWxUnionId) ? null : shareWxUnionId.trim();
    }

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = StringUtil.isEmpty(userWxUnionId) ? null : userWxUnionId.trim();
    }

    public String getUserWxOpenId() {
        return userWxOpenId;
    }

    public void setUserWxOpenId(String userWxOpenId) {
        this.userWxOpenId = StringUtil.isEmpty(userWxOpenId) ? null : userWxOpenId.trim();
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = StringUtil.isEmpty(pageUrl) ? null : pageUrl.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = StringUtil.isEmpty(ip) ? null : ip.trim();
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Boolean getKeepOnRecordUser() {
        return keepOnRecordUser;
    }

    public void setKeepOnRecordUser(Boolean keepOnRecordUser) {
        this.keepOnRecordUser = keepOnRecordUser;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Boolean getFinishFissionTask() {
        return finishFissionTask;
    }

    public void setFinishFissionTask(Boolean finishFissionTask) {
        this.finishFissionTask = finishFissionTask;
    }

    public Integer getFissionLevel() {
        return fissionLevel;
    }

    public void setFissionLevel(Integer fissionLevel) {
        this.fissionLevel = fissionLevel;
    }

    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Integer> getOrderStatusList() {
        return orderStatusList;
    }

    public void setOrderStatusList(List<Integer> orderStatusList) {
        this.orderStatusList = orderStatusList;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Integer getCheckedSalesId() {
        return checkedSalesId;
    }

    public void setCheckedSalesId(Integer checkedSalesId) {
        this.checkedSalesId = checkedSalesId;
    }

    public Integer getCheckedDealerId() {
        return checkedDealerId;
    }

    public void setCheckedDealerId(Integer checkedDealerId) {
        this.checkedDealerId = checkedDealerId;
    }

    public Date getCheckedDate() {
        return checkedDate;
    }

    public void setCheckedDate(Date checkedDate) {
        this.checkedDate = checkedDate;
    }

    public Integer getCbId() {
        return cbId;
    }

    public void setCbId(Integer cbId) {
        this.cbId = cbId;
    }

    public Integer getCsId() {
        return csId;
    }

    public void setCsId(Integer csId) {
        this.csId = csId;
    }

    public Integer getSubAccountStatus() {
        return subAccountStatus;
    }

    public void setSubAccountStatus(Integer subAccountStatus) {
        this.subAccountStatus = subAccountStatus;
    }

    public BigDecimal getSubAccountAmount() {
        return subAccountAmount;
    }

    public void setSubAccountAmount(BigDecimal subAccountAmount) {
        this.subAccountAmount = subAccountAmount;
    }

    public Date getSubAccountTime() {
        return subAccountTime;
    }

    public void setSubAccountTime(Date subAccountTime) {
        this.subAccountTime = subAccountTime;
    }

    public String getSubAccountTransactionDetailId() {
        return subAccountTransactionDetailId;
    }

    public void setSubAccountTransactionDetailId(String subAccountTransactionDetailId) {
        this.subAccountTransactionDetailId = subAccountTransactionDetailId;
    }

    public String getSubAccountResultCode() {
        return subAccountResultCode;
    }

    public void setSubAccountResultCode(String subAccountResultCode) {
        this.subAccountResultCode = subAccountResultCode;
    }

    public String getSubAccountReceiverResult() {
        return subAccountReceiverResult;
    }

    public void setSubAccountReceiverResult(String subAccountReceiverResult) {
        this.subAccountReceiverResult = subAccountReceiverResult;
    }

    public String getSubAccountDesc() {
        return subAccountDesc;
    }

    public void setSubAccountDesc(String subAccountDesc) {
        this.subAccountDesc = subAccountDesc;
    }

    public String getSubAccountReturnNo() {
        return subAccountReturnNo;
    }

    public void setSubAccountReturnNo(String subAccountReturnNo) {
        this.subAccountReturnNo = subAccountReturnNo;
    }

    public Date getSubAccountReturnTime() {
        return subAccountReturnTime;
    }

    public void setSubAccountReturnTime(Date subAccountReturnTime) {
        this.subAccountReturnTime = subAccountReturnTime;
    }

    public String getWxTransactionId() {
        return wxTransactionId;
    }

    public void setWxTransactionId(String wxTransactionId) {
        this.wxTransactionId = wxTransactionId;
    }

    public String getTcTransactionId() {
        return tcTransactionId;
    }

    public void setTcTransactionId(String tcTransactionId) {
        this.tcTransactionId = tcTransactionId;
    }
}