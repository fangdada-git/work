package com.tuanche.directselling.model;

import java.io.Serializable;

/**
 * fission_activity_data
 * @author
 */
public class FissionActivityData implements Serializable {

    private Integer id;

    /**
     * 裂变活动id
     */
    private Integer fissionId;

    /**
     * 浏览基数
     */
    private Integer browseBase;

    /**
     * 浏览系数（默认1）
     */
    private Integer browseCoefficient;

    /**
     * 真实浏览数
     */
    private Integer browseNum;

    /**
     * 预约直播基数
     */
    private Integer subscribeBase;

    /**
     * 预约直播系数（默认1）
     */
    private Integer subscribeCoefficient;

    /**
     * 真实预约数
     */
    private Integer subscribeNum;

    /**
     * 分享基数
     */
    private Integer shareBase;

    /**
     * 分享系数（默认1）
     */
    private Integer shareCoefficient;

    /**
     * 真实分享数
     */
    private Integer shareNum;

    private static final long serialVersionUID = 1L;

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

    public Integer getBrowseBase() {
        return browseBase;
    }

    public void setBrowseBase(Integer browseBase) {
        this.browseBase = browseBase;
    }

    public Integer getBrowseCoefficient() {
        return browseCoefficient;
    }

    public void setBrowseCoefficient(Integer browseCoefficient) {
        this.browseCoefficient = browseCoefficient;
    }

    public Integer getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(Integer browseNum) {
        this.browseNum = browseNum;
    }

    public Integer getSubscribeBase() {
        return subscribeBase;
    }

    public void setSubscribeBase(Integer subscribeBase) {
        this.subscribeBase = subscribeBase;
    }

    public Integer getSubscribeCoefficient() {
        return subscribeCoefficient;
    }

    public void setSubscribeCoefficient(Integer subscribeCoefficient) {
        this.subscribeCoefficient = subscribeCoefficient;
    }

    public Integer getSubscribeNum() {
        return subscribeNum;
    }

    public void setSubscribeNum(Integer subscribeNum) {
        this.subscribeNum = subscribeNum;
    }

    public Integer getShareBase() {
        return shareBase;
    }

    public void setShareBase(Integer shareBase) {
        this.shareBase = shareBase;
    }

    public Integer getShareCoefficient() {
        return shareCoefficient;
    }

    public void setShareCoefficient(Integer shareCoefficient) {
        this.shareCoefficient = shareCoefficient;
    }

    public Integer getShareNum() {
        return shareNum;
    }

    public void setShareNum(Integer shareNum) {
        this.shareNum = shareNum;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        FissionActivityData other = (FissionActivityData) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getFissionId() == null ? other.getFissionId() == null : this.getFissionId().equals(other.getFissionId()))
                && (this.getBrowseBase() == null ? other.getBrowseBase() == null : this.getBrowseBase().equals(other.getBrowseBase()))
                && (this.getBrowseCoefficient() == null ? other.getBrowseCoefficient() == null : this.getBrowseCoefficient().equals(other.getBrowseCoefficient()))
                && (this.getBrowseNum() == null ? other.getBrowseNum() == null : this.getBrowseNum().equals(other.getBrowseNum()))
                && (this.getSubscribeBase() == null ? other.getSubscribeBase() == null : this.getSubscribeBase().equals(other.getSubscribeBase()))
                && (this.getSubscribeCoefficient() == null ? other.getSubscribeCoefficient() == null : this.getSubscribeCoefficient().equals(other.getSubscribeCoefficient()))
                && (this.getSubscribeNum() == null ? other.getSubscribeNum() == null : this.getSubscribeNum().equals(other.getSubscribeNum()))
                && (this.getShareBase() == null ? other.getShareBase() == null : this.getShareBase().equals(other.getShareBase()))
                && (this.getShareCoefficient() == null ? other.getShareCoefficient() == null : this.getShareCoefficient().equals(other.getShareCoefficient()))
                && (this.getShareNum() == null ? other.getShareNum() == null : this.getShareNum().equals(other.getShareNum()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFissionId() == null) ? 0 : getFissionId().hashCode());
        result = prime * result + ((getBrowseBase() == null) ? 0 : getBrowseBase().hashCode());
        result = prime * result + ((getBrowseCoefficient() == null) ? 0 : getBrowseCoefficient().hashCode());
        result = prime * result + ((getBrowseNum() == null) ? 0 : getBrowseNum().hashCode());
        result = prime * result + ((getSubscribeBase() == null) ? 0 : getSubscribeBase().hashCode());
        result = prime * result + ((getSubscribeCoefficient() == null) ? 0 : getSubscribeCoefficient().hashCode());
        result = prime * result + ((getSubscribeNum() == null) ? 0 : getSubscribeNum().hashCode());
        result = prime * result + ((getShareBase() == null) ? 0 : getShareBase().hashCode());
        result = prime * result + ((getShareCoefficient() == null) ? 0 : getShareCoefficient().hashCode());
        result = prime * result + ((getShareNum() == null) ? 0 : getShareNum().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", fissionId=").append(fissionId);
        sb.append(", browseBase=").append(browseBase);
        sb.append(", browseCoefficient=").append(browseCoefficient);
        sb.append(", browseNum=").append(browseNum);
        sb.append(", subscribeBase=").append(subscribeBase);
        sb.append(", subscribeCoefficient=").append(subscribeCoefficient);
        sb.append(", subscribeNum=").append(subscribeNum);
        sb.append(", shareBase=").append(shareBase);
        sb.append(", shareCoefficient=").append(shareCoefficient);
        sb.append(", shareNum=").append(shareNum);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}