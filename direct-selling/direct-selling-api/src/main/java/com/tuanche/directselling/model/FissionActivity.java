package com.tuanche.directselling.model;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author lvsen
 * @description 裂变活动实体
 * @date 2020/9/22 15:46
 */
public class FissionActivity implements Serializable {

    private static final long serialVersionUID = 231065160675464806L;

    private Integer id;
    /**
     * 场次id
     */
    private Integer periodsId;
    /**
     * 场次名称
     */
    private String periodsName;
    /**
     * 小场次id
     */
    private Integer sceneId;
    private List<Integer> sceneIdList;
    /**
     * 活动名称
     */
    private String activityName;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 开启状态
     */
    private Short onState;
    /**
     * 直播间id
     */
    private Integer broadcastRoomId;
    /**
     * 头图
     */
    private String headPicUrl;
    /**
     * 分离图
     */
    private String sharePicUrl;
    /**
     * 详情图
     */
    private String detailPicUrls;
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
     * 微信分享图片
     */
    private String wechatPic;
    /**
     * 删除状态
     */
    private Byte deleteFlag;
    /**
     * 创建人
     */
    private Integer ctreateBy;
    /**
     * 创建时间
     */
    private Date ctreateDt;
    /**
     * 修改人
     */
    private Integer updateBy;
    /**
     * 修改时间
     */
    private Date updateDt;

    /**
     * 0:B端数据 1:C端数据 2:不带销售标识 3:无分享人(浏览总人数、次只是无销售标识的)
     * 2进制数组
     */
    private Integer statGenerate;

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

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

    public String getPeriodsName() {
        return periodsName;
    }

    public void setPeriodsName(String periodsName) {
        this.periodsName = periodsName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Short getOnState() {
        return onState;
    }

    public void setOnState(Short onState) {
        this.onState = onState;
    }

    public Integer getBroadcastRoomId() {
        return broadcastRoomId;
    }

    public void setBroadcastRoomId(Integer broadcastRoomId) {
        this.broadcastRoomId = broadcastRoomId;
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

    public String getDetailPicUrls() {
        return detailPicUrls;
    }

    public void setDetailPicUrls(String detailPicUrls) {
        this.detailPicUrls = detailPicUrls;
    }

    public String getProductTitleUrl() {
        return productTitleUrl;
    }

    public void setProductTitleUrl(String productTitleUrl) {
        this.productTitleUrl = productTitleUrl;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public Short getReservePhone() {
        return reservePhone;
    }

    public void setReservePhone(Short reservePhone) {
        this.reservePhone = reservePhone;
    }

    public Byte getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Byte deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getCtreateBy() {
        return ctreateBy;
    }

    public void setCtreateBy(Integer ctreateBy) {
        this.ctreateBy = ctreateBy;
    }

    public Date getCtreateDt() {
        return ctreateDt;
    }

    public void setCtreateDt(Date ctreateDt) {
        this.ctreateDt = ctreateDt;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
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

    public Integer getcAwardFlag() {
        return cAwardFlag;
    }

    public void setcAwardFlag(Integer cAwardFlag) {
        this.cAwardFlag = cAwardFlag;
    }

    public Integer getStatGenerate() {
        return statGenerate;
    }

    public void setStatGenerate(Integer statGenerate) {
        this.statGenerate = statGenerate;
    }

    public List<Integer> getSceneIdList() {
        return sceneIdList;
    }

    public void setSceneIdList(List<Integer> sceneIdList) {
        this.sceneIdList = sceneIdList;
    }
}