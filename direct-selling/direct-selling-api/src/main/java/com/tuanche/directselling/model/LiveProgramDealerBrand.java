package com.tuanche.directselling.model;

import java.io.Serializable;
import java.util.Date;

/**
 * live_program_dealer_brand
 * @author 
 */
public class LiveProgramDealerBrand implements Serializable {
    private Integer id;

    /**
     * 场次活动ID
     */
    private Integer sceneId;

    /**
     * 节目ID
     */
    private Integer programId;
    private Integer broadcastId;

    /**
     * 场次活动下经销商品牌ID
     */
    private Integer dealerBrandId;

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
    private Integer deleteState;
    /**
     * 机位类型（1:主会场，0：分会场）
     */
    private Integer programType;

    private static final long serialVersionUID = 1L;

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

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public Integer getDealerBrandId() {
        return dealerBrandId;
    }

    public void setDealerBrandId(Integer dealerBrandId) {
        this.dealerBrandId = dealerBrandId;
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

	public Integer getBroadcastId() {
		return broadcastId;
	}

	public void setBroadcastId(Integer broadcastId) {
		this.broadcastId = broadcastId;
	}

	public Integer getDeleteState() {
		return deleteState;
	}

	public void setDeleteState(Integer deleteState) {
		this.deleteState = deleteState;
	}

	public Integer getProgramType() {
		return programType;
	}

	public void setProgramType(Integer programType) {
		this.programType = programType;
	}

}