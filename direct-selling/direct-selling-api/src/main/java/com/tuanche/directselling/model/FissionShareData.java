package com.tuanche.directselling.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * fission_share_data
 * @author 
 */
public class FissionShareData implements Serializable {

    public FissionShareData() {}

    public FissionShareData(Integer fissionId, String saleWxUnionId, String userWxUnionId) {
        this.fissionId = fissionId;
        this.saleWxUnionId = saleWxUnionId;
        this.userWxUnionId = userWxUnionId;
    }

    private Integer id;

    /**
     * 裂变活动id
     */
    private Integer fissionId;

    /**
     * 销售微信UnionId
     */
    private String saleWxUnionId;

    /**
     * 分享人微信UnionId
     */
    private String userWxUnionId;

    /**
     * 分享人微信昵称
     */
    private String userWxNick;

    /**
     * 分享地址
     */
    private String shareUrl;

    /**
     * 微信分享海报图地址
     */
    private String sharePosterUrl;

    /**
     * 删除标识
     */
    private Boolean deleteFlag;

    /**
     * 创建时间
     */
    private Date createDt;

    /**
     * 更新时间
     */
    private Date updateDt;

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

    public String getSaleWxUnionId() {
        return saleWxUnionId;
    }

    public void setSaleWxUnionId(String saleWxUnionId) {
        this.saleWxUnionId = saleWxUnionId;
    }

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId;
    }

    public String getUserWxNick() {
        return userWxNick;
    }

    public void setUserWxNick(String userWxNick) {
        this.userWxNick = userWxNick;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getSharePosterUrl() {
        return sharePosterUrl;
    }

    public void setSharePosterUrl(String sharePosterUrl) {
        this.sharePosterUrl = sharePosterUrl;
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

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FissionShareData that = (FissionShareData) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(fissionId, that.fissionId) &&
                Objects.equals(saleWxUnionId, that.saleWxUnionId) &&
                Objects.equals(userWxUnionId, that.userWxUnionId) &&
                Objects.equals(userWxNick, that.userWxNick) &&
                Objects.equals(shareUrl, that.shareUrl) &&
                Objects.equals(sharePosterUrl, that.sharePosterUrl) &&
                Objects.equals(deleteFlag, that.deleteFlag) &&
                Objects.equals(createDt, that.createDt) &&
                Objects.equals(updateDt, that.updateDt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fissionId, saleWxUnionId, userWxUnionId, userWxNick, shareUrl, sharePosterUrl, deleteFlag, createDt, updateDt);
    }
}