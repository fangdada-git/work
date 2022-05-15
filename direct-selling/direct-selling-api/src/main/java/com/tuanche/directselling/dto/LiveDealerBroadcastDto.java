package com.tuanche.directselling.dto;

import com.tuanche.arch.util.utils.DateUtils;
import com.tuanche.directselling.model.LiveDealerBroadcast;

import java.util.List;

public class LiveDealerBroadcastDto extends LiveDealerBroadcast  {

	private static final long serialVersionUID = -329383171429264984L;

	/**
	 * 场次开始时间戳 毫秒
	 */
	private Long startTimeStamp;
	/**
	 * 场次结束时间戳 毫秒
	 */
	private Long endTimeStamp;
	/**
	 * 场次预告时间戳 毫秒
	 */
	private Long preheatTimeStamp;

	private String periodsEndTime;
	
	private String periodsStartTime;

	private String periodsPreheatTime;

	private Long[] anchorIds;
	
	private Integer cityId;
	
	private Integer periodsId;
	
	/** 经销商ID */
	private Long dealerId;
	
	/** 品牌ID*/
	private Integer brandId;
	
	/** 主播地址*/
	private String anchorUrl;
	/** 主播昵称*/
	private String anchorNick;
	
	/** 主播头像 */
	private String headImg;

	/**
	 * 参展城市ids
	 */
	private List<Integer> cityIds;

	public Long getStartTimeStamp() {
		return startTimeStamp;
	}

	public void setStartTimeStamp(Long startTimeStamp) {
		this.startTimeStamp = startTimeStamp;
	}

	public Long getEndTimeStamp() {
		return endTimeStamp;
	}

	public void setEndTimeStamp(Long endTimeStamp) {
		this.endTimeStamp = endTimeStamp;
	}

	public Long getPreheatTimeStamp() {
		return preheatTimeStamp;
	}

	public void setPreheatTimeStamp(Long preheatTimeStamp) {
		this.preheatTimeStamp = preheatTimeStamp;
	}

	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public Long getDealerId() {
		return dealerId;
	}
	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}
	public Integer getBrandId() {
		return brandId;
	}
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	public String getAnchorUrl() {
		return anchorUrl;
	}
	public void setAnchorUrl(String anchorUrl) {
		this.anchorUrl = anchorUrl;
	}
	public String getAnchorNick() {
		return anchorNick;
	}
	public void setAnchorNick(String anchorNick) {
		this.anchorNick = anchorNick;
	}
	public Integer getPeriodsId() {
		return periodsId;
	}
	public void setPeriodsId(Integer periodsId) {
		this.periodsId = periodsId;
	}
	public String getPeriodsEndTime() {
		if (endTimeStamp != null) {
			return DateUtils.dateToString(DateUtils.LongToDate(endTimeStamp),DateUtils.YYYY_MM_DD_HH_MM_SS);
		}
		return periodsEndTime;
	}
	public void setPeriodsEndTime(String periodsEndTime) {
		this.periodsEndTime = periodsEndTime;
	}
	public String getPeriodsStartTime() {
		if (startTimeStamp != null) {
			return DateUtils.dateToString(DateUtils.LongToDate(startTimeStamp),DateUtils.YYYY_MM_DD_HH_MM_SS);
		}
		return periodsStartTime;
	}
	public void setPeriodsStartTime(String periodsStartTime) {
		this.periodsStartTime = periodsStartTime;
	}

	public String getPeriodsPreheatTime() {
		if (preheatTimeStamp != null) {
			return DateUtils.dateToString(DateUtils.LongToDate(preheatTimeStamp),DateUtils.YYYY_MM_DD_HH_MM_SS);
		}
		return periodsPreheatTime;
	}

	public void setPeriodsPreheatTime(String periodsPreheatTime) {
		this.periodsPreheatTime = periodsPreheatTime;
	}

	public Long[] getAnchorIds() {
		return anchorIds;
	}
	public void setAnchorIds(Long[] anchorIds) {
		this.anchorIds = anchorIds;
	}

	public List<Integer> getCityIds() {
		return cityIds;
	}

	public void setCityIds(List<Integer> cityIds) {
		this.cityIds = cityIds;
	}
}
