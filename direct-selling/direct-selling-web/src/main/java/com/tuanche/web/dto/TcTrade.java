package com.tuanche.web.dto;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;

public class TcTrade implements Serializable {
	
	private Integer id;
	private String tradeId;   //团车流水号       必传(退款)传支付流水号 (收银台 序列号、流水号（退款用）)
	private String orderNo;  //订单号     必传(付款退款)
	private String content;   //商品描述     必传(付款退款)
	private BigDecimal amount; //支付金额   必传(付款退款) 小数点后最多两位
	private String payType;  //类型  01-付款  02-退款     必传(付款退款)
	private String productCode; //渠道编码 08，28  必传(付款)
	private String code;  //支付或退款方式，选择支付方式后更新
	private String status; //支付状态
	private String statusDesc;  //状态描述
	private String remark; //备注
	private String returnUrl;  //支付成功回调地址       必传(付款)
	private String notifyUrl;  //回调通知地址           必传(付款退款)
	private String backUrl;  //支付页面返回按钮地址     必传(付款)
	private String createTime; //创建时间
	private String updateTime; //更新时间
	private String openId;
	private String resId; //第三方流水号
	private String payMethod = "0"; //付款类型 0-在线支付 1-pos 2-现金
	private String broswerType = "1"; // 0 PC 1 Wap 必传(付款)
	
	//业务方流水号 (标识业务)
	private String tradeBusiness; //业务流水号  必传(付款退款)
	
	//条件查询用字段
	private String timeStart;
	private String timeEnd;
	private String account;
	private String orgResId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTradeId() {
		return tradeId;
	}
	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getBackUrl() {
		return backUrl;
	}
	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getResId() {
		return resId;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}
	
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		if(StringUtils.isEmpty(payMethod)) {
			this.payMethod = "0";
		} else {
			this.payMethod = payMethod;
		}
	}
	public String getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}
	public String getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getOrgResId() {
		return orgResId;
	}
	public void setOrgResId(String orgResId) {
		this.orgResId = orgResId;
	}
	public String getBroswerType() {
		return broswerType;
	}
	public void setBroswerType(String broswerType) {
		if(StringUtils.isEmpty(broswerType)) {
			this.broswerType = "1";
		} else {
			this.broswerType = broswerType;
		}
	}
	public String getTradeBusiness() {
		return tradeBusiness;
	}
	public void setTradeBusiness(String tradeBusiness) {
		this.tradeBusiness = tradeBusiness;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
