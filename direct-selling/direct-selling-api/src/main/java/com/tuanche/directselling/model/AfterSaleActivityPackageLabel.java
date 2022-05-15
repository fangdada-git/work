package com.tuanche.directselling.model;

import java.io.Serializable;
import java.util.Date;

public class AfterSaleActivityPackageLabel implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 套餐id
     */
    private Integer packageId;

    /**
     * 一级标签id
     */
    private Integer primaryLabelId;

    /**
     * 二级标签id
     */
    private Integer secondaryLabelId;

    /**
     * 是否删除 0未删除 1 删除
     */
    private Boolean deleteFlag;

    /**
     * 创建时间
     */
    private Date createDt;

    /**
     * 创建人
     */
    private String createName;

    /**
     * 更新时间
     */
    private Date updateDt;

    /**
     * 更新人
     */
    private String updateName;

    /**
     * after_sale_activity_package_label
     */
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public Integer getPrimaryLabelId() {
        return primaryLabelId;
    }

    public void setPrimaryLabelId(Integer primaryLabelId) {
        this.primaryLabelId = primaryLabelId;
    }

    public Integer getSecondaryLabelId() {
        return secondaryLabelId;
    }

    public void setSecondaryLabelId(Integer secondaryLabelId) {
        this.secondaryLabelId = secondaryLabelId;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
}