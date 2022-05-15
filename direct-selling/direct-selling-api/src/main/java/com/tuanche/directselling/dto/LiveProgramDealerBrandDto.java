package com.tuanche.directselling.dto;

import java.io.Serializable;

import com.tuanche.directselling.model.LiveProgramDealerBrand;

/**
 * 
 * @Description 品牌经销商
 * @author zhangxing
 * @version   
 * @date 2020年3月5日上午11:49:42
 */
public class LiveProgramDealerBrandDto extends LiveProgramDealerBrand implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer brandId;
	private Integer dealerId;
	public Integer getBrandId() {
		return brandId;
	}
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	public Integer getDealerId() {
		return dealerId;
	}
	public void setDealerId(Integer dealerId) {
		this.dealerId = dealerId;
	}
	
}
