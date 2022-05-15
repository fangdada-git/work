package com.tuanche.directselling.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 裂变活动扩展表
 * @date 2020/9/22 16:34
 * @author lvsen
 */
public class FissionActivityExtend implements Serializable {

    private static final long serialVersionUID = 8464290244032412044L;
    private Integer id;

    /**
     * 裂变id
     */
    private Integer fissionId;

    /**
     * 数据类型（1城市 2商品 3经销商）
     */
    private Byte dataType;
    /**
     * 对应数据id（类型是城市，数据id为-1代表全国）
     */
    private Integer dataId;
    /**
     * 关联状态（1关联 2取消关联）
     */
    private Byte relStatus;
    /**
     * 关联时间
     */
    private Date relTime;
    /**
     * 操作人
     */
    private Integer operateUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
    }

    public Byte getDataType() {
        return dataType;
    }

    public void setDataType(Byte dataType) {
        this.dataType = dataType;
    }

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public Byte getRelStatus() {
        return relStatus;
    }

    public void setRelStatus(Byte relStatus) {
        this.relStatus = relStatus;
    }

    public Date getRelTime() {
        return relTime;
    }

    public void setRelTime(Date relTime) {
        this.relTime = relTime;
    }

    public Integer getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(Integer operateUser) {
        this.operateUser = operateUser;
    }
}