package com.tuanche.directselling.model;

import java.io.Serializable;
import java.util.Date;

/**
 * live_scene_dealer_config
 * @author 
 */
public class LiveSceneDealerConfig implements Serializable {
    private Integer id;

    /**
     * 大场次ID
     */
    private Integer periodsId;

    /**
     * 场次活动ID
     */
    private Integer sceneId;

    /**
     * 经销商ID
     */
    private Integer dealerId;

    /**
     * 经销商名称
     */
    private String dealerName;
    //合同经销商名称
    private String contractDealerName;

    /**
     * 保量值
     */
    private Integer ensureSize;

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
    private Boolean deleteState;
    /**
     * 分机位码
     */
    private String extensionCode;

    /**
     * 每个场次赠送油卡上限
     */
    private Integer refuelingCardNum;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPeriodsId() {
        return periodsId;
    }

    public void setPeriodsId(Integer periodsId) {
        this.periodsId = periodsId;
    }

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public Integer getEnsureSize() {
        return ensureSize;
    }

    public void setEnsureSize(Integer ensureSize) {
        this.ensureSize = ensureSize;
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

    public Boolean getDeleteState() {
        return deleteState;
    }

    public void setDeleteState(Boolean deleteState) {
        this.deleteState = deleteState;
    }

    public String getExtensionCode() {
        return extensionCode;
    }

    public void setExtensionCode(String extensionCode) {
        this.extensionCode = extensionCode;
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
    public Integer getRefuelingCardNum() {
        return refuelingCardNum;
    }

    public void setRefuelingCardNum(Integer refuelingCardNum) {
        this.refuelingCardNum = refuelingCardNum;
    }
}