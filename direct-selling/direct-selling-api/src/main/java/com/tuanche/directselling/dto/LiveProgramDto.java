package com.tuanche.directselling.dto;

import java.io.Serializable;
import java.util.List;

import com.tuanche.directselling.model.LiveProgram;
import com.tuanche.directselling.model.LiveSceneDealerBrand;

/**
 * 
 * @Description 节目Dto
 * @author zhangxing
 * @version   
 * @date 2020年3月5日上午11:49:42
 */
public class LiveProgramDto extends LiveProgram implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<LiveSceneDealerBrand> LiveSceneDealerBrands;
	private Integer brandId;

	public List<LiveSceneDealerBrand> getLiveSceneDealerBrands() {
		return LiveSceneDealerBrands;
	}

	public void setLiveSceneDealerBrands(List<LiveSceneDealerBrand> liveSceneDealerBrands) {
		LiveSceneDealerBrands = liveSceneDealerBrands;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

}
