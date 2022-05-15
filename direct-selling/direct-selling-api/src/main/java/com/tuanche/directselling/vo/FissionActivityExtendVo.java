package com.tuanche.directselling.vo;/**
 * @program: direct-selling
 * @description: 裂变活动扩展vo
 * @author: lvsen
 * @create: 2020-09-30 13:59
 **/

import java.io.Serializable;
import java.util.List;

/**
 * @Author lvsen
 * @Description
 * @Date 2020/9/30
 **/
public class FissionActivityExtendVo implements Serializable {
    private static final long serialVersionUID = 1155323624200674587L;

    /**
     * 裂变id
     */
    private Integer fissionId;

    /**
     * 头图
     */
    private String headPicUrl;

    /**
     * 分享图
     */
    private String sharePicUrl;
    /**
     * 详情图1
     */
    private String detailPicUrl1;

    /**
     * 详情图2
     */
    private String detailPicUrl2;

    /**
     * 详情图3
     */
    private String detailPicUrl3;
    /**
     * 海报图
     */
    private String posterUrl;

    /**
     * 商品标题图
     */
    private String productTitleUrl;

    /**
     * 是否保留手机号
     */
    private Short reservePhone;

    /**
     * 数据类型（1城市 2商品 3经销商）
     */
    private Byte dataType;
    /**
     * 对应数据id
     */
    private Integer dataId;

    /**
     * 操作人
     */
    private Integer operateUser;

    /**
     * 城市list
     */
    private String cityIds;

    /**
     * 商品list
     */
    private String productIds;

    /**
     * 浏览基数
     */
    private Integer browseBase;
    /**
     * 浏览系数
     */
    private Integer browseCoefficient;
    /**
     * 预约直播基数
     */
    private Integer subscribeBase;
    /**
     * 预约直播系数
     */
    private Integer subscribeCoefficient;
    /**
     * 分享基数
     */
    private Integer shareBase;
    /**
     * 分享系数
     */
    private Integer shareCoefficient;
    /**
     * C端是否现金奖励（1是 0否）
     */
    private Integer cAwardFlag;
    /**
     * 微信分享标题
     */
    private String wechatTitle;
    /**
     * 微信分享描述
     */
    private String wechatDescription;
    /**
     * 微信分享图
     */
    private String wechatPic;
    /**
     * 开启状态
     */
    private Short onState;

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
    }

    public String getHeadPicUrl() {
        return headPicUrl;
    }

    public void setHeadPicUrl(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }

    public String getSharePicUrl() {
        return sharePicUrl;
    }

    public void setSharePicUrl(String sharePicUrl) {
        this.sharePicUrl = sharePicUrl;
    }

    public String getDetailPicUrl1() {
        return detailPicUrl1;
    }

    public void setDetailPicUrl1(String detailPicUrl1) {
        this.detailPicUrl1 = detailPicUrl1;
    }

    public String getDetailPicUrl2() {
        return detailPicUrl2;
    }

    public void setDetailPicUrl2(String detailPicUrl2) {
        this.detailPicUrl2 = detailPicUrl2;
    }

    public String getDetailPicUrl3() {
        return detailPicUrl3;
    }

    public void setDetailPicUrl3(String detailPicUrl3) {
        this.detailPicUrl3 = detailPicUrl3;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getProductTitleUrl() {
        return productTitleUrl;
    }

    public void setProductTitleUrl(String productTitleUrl) {
        this.productTitleUrl = productTitleUrl;
    }

    public Integer getcAwardFlag() {
        return cAwardFlag;
    }

    public void setcAwardFlag(Integer cAwardFlag) {
        this.cAwardFlag = cAwardFlag;
    }

    public Short getReservePhone() {
        return reservePhone;
    }

    public void setReservePhone(Short reservePhone) {
        this.reservePhone = reservePhone;
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

    public Integer getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(Integer operateUser) {
        this.operateUser = operateUser;
    }

    public String getCityIds() {
        return cityIds;
    }

    public void setCityIds(String cityIds) {
        this.cityIds = cityIds;
    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
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

    public Short getOnState() {
        return onState;
    }

    public void setOnState(Short onState) {
        this.onState = onState;
    }

    public String getWechatTitle() {
        return wechatTitle;
    }

    public void setWechatTitle(String wechatTitle) {
        this.wechatTitle = wechatTitle;
    }

    public String getWechatDescription() {
        return wechatDescription;
    }

    public void setWechatDescription(String wechatDescription) {
        this.wechatDescription = wechatDescription;
    }

    public String getWechatPic() {
        return wechatPic;
    }

    public void setWechatPic(String wechatPic) {
        this.wechatPic = wechatPic;
    }

}
