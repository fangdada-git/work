package com.tuanche.directselling.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tuanche.directselling.dto.LiveProgramDealerBrandDto;

/**
 * 
 * @Description 节目VO
 * @author zhangxing
 * @version   
 * @date 2020年3月5日下午5:41:50
 */
public class LiveProgramVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;

    /**
     * 场次活动ID
     */
    private Integer sceneId;

    /**
     * 节目名称
     */
    private String programTitle;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 直播地址
     */
    private String liveAddress;

    /**
     * 添加人ID
     */
    private Integer createUserId;

    /**
     * 添加人姓名
     */
    private String createUserName;

    /**
     * 添加时间
     */
    private Date createDt;

    /**
     * 编辑人ID
     */
    private Integer updateUserId;

    /**
     * 编辑人姓名
     */
    private String updateUserName;

    /**
     * 编辑时间
     */
    private Date updateDt;

    /**
     * 删除状态（1:是  0:否）
     */
    private int deleteState;

	/**
	 * 场次活动开始时间
	 */
    private Date sceneBeginTime;

	/**
	 * 场次活动结束时间
	 */
    private Date sceneEndTime;
    
    private Integer brandId;
	/**
	 * 直播节目类型（1:预热直播  2:正式直播）
	 */
	private Integer programType;
    private List<LiveProgramDealerBrandDto> programDealerBrands;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSceneId() {
		return sceneId;
	}

	public void setSceneId(Integer sceneId) {
		this.sceneId = sceneId;
	}

	public String getProgramTitle() {
		return programTitle;
	}

	public void setProgramTitle(String programTitle) {
		this.programTitle = programTitle;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getLiveAddress() {
		return liveAddress;
	}

	public void setLiveAddress(String liveAddress) {
		this.liveAddress = liveAddress;
	}

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public Integer getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Integer updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public Date getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	public int getDeleteState() {
		return deleteState;
	}

	public void setDeleteState(int deleteState) {
		this.deleteState = deleteState;
	}

	public List<LiveProgramDealerBrandDto> getProgramDealerBrands() {
		return programDealerBrands;
	}

	public void setProgramDealerBrands(List<LiveProgramDealerBrandDto> programDealerBrands) {
		this.programDealerBrands = programDealerBrands;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public Date getSceneBeginTime() {
		return sceneBeginTime;
	}

	public void setSceneBeginTime(Date sceneBeginTime) {
		this.sceneBeginTime = sceneBeginTime;
	}

	public Date getSceneEndTime() {
		return sceneEndTime;
	}

	public void setSceneEndTime(Date sceneEndTime) {
		this.sceneEndTime = sceneEndTime;
	}

	public Integer getProgramType() {
		return programType;
	}

	public void setProgramType(Integer programType) {
		this.programType = programType;
	}
}
