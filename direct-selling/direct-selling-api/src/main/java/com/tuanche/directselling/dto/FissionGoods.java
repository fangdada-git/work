package com.tuanche.directselling.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FissionGoods implements Serializable {

    private static final long serialVersionUID = 8206060277187680955L;
    //直播场次id
    private Integer peroidsId;
    //裂变活动id
    private Integer fissionId;
    /**
     * 商品id
     */
    private Integer commodityId;

    /**
     * 商品名称
     */
    private String commodityName;

    /**
     * 商品价格
     */
    private BigDecimal commodityPrice;

    /**
     * 商品的玩法（如 订金、代金券、到店礼）
     */
    private FissionGoodsTypeEnum commodityType;

    /**
     * 付款方式（如：一次性付款、支持o2o订金交易）
     */
    private String paymentType;

    /**
     * 商品数量
     */
    private Integer commodityCount;

    /**
     * 已售商品数量
     */
    private Integer soldNumber;

    /**
     * 已售虚拟商品数量
     */
    private Integer virtualSoldNumber;

    /**
     * 真实售卖数据+虚拟售卖数量
     */
    private Integer totalSoldNumber;

    /**
     * 商品所在地
     */
    private Integer commodityAddress;

    /**
     * 是否有发票 默认是无发票
     */
    private Integer invoiceFlag;

    /**
     * 会员让利（0 不让利  1 让利）
     */
    private Integer memberDiscountsFlag;

    /**
     * 立减库存（0 否 1 是）
     */
    private Integer reduceInventoryFlag;

    /**
     * 上架形式 0:下架  1:上架
     */
    private Integer putAwayType;

    /**
     * 商品标价
     */
    private BigDecimal originalPrice;

    /**
     * 是否秒杀类商品 0：否   1：是
     */
    private Integer seckill;

    /**
     * 是否需要邮寄  0：否    1：是
     */
    private Integer isPost;

    //立即上架
    private Integer upShelf;
    /**
     * 定时上架时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date upShelfTime;

    //活动结束下架
    private Integer downShelf;
    /**
     * 定时下架时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date downShelfTime;
    
    /**
     * 商品详情 详情图和文字描述
     */
    private String commodityDetail;

    /**
     * 商品来源： 1:直卖  2：裂变活动
     */
    private Integer commoditySource;

    /**
     * 创建时间
     */
    private Date createDt;

    /**
     * 更新时间
     */
    private Date updateDt;

    /**
     * 逻辑删除状态  1:逻辑删除  0:有效数据
     */
    private Integer deleteFlag;

    /**
     * 商品描述信息
     */
    private String description;
    /**
     * 头图图片详情
     */
    private List<FissionGoodsImageDto> headImages;

    /**
     * 详情图图片列表
     */
    private List<FissionGoodsImageDto> descpritionImages;
    //是否助力  1：是  0：fou
    private Integer isHelper;
    //助力人数
    private Integer helperNum;
    //库存不足时未付款标识    1：是|有 0：否|无
    private Integer nonPay;
    //品牌列表
    private List<FissionBrandDto> brandDtoList;

    private List<FissionStyleDto> styleDtoList;

    public Integer getPeroidsId() {
        return peroidsId;
    }

    public void setPeroidsId(Integer peroidsId) {
        this.peroidsId = peroidsId;
    }

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
    }

    public Integer getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Integer commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public BigDecimal getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(BigDecimal commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public FissionGoodsTypeEnum getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(FissionGoodsTypeEnum commodityType) {
        this.commodityType = commodityType;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getCommodityCount() {
        return commodityCount;
    }

    public void setCommodityCount(Integer commodityCount) {
        this.commodityCount = commodityCount;
    }

    public Integer getCommodityAddress() {
        return commodityAddress;
    }

    public void setCommodityAddress(Integer commodityAddress) {
        this.commodityAddress = commodityAddress;
    }

    public Integer getInvoiceFlag() {
        return invoiceFlag;
    }

    public void setInvoiceFlag(Integer invoiceFlag) {
        this.invoiceFlag = invoiceFlag;
    }

    public Integer getMemberDiscountsFlag() {
        return memberDiscountsFlag;
    }

    public void setMemberDiscountsFlag(Integer memberDiscountsFlag) {
        this.memberDiscountsFlag = memberDiscountsFlag;
    }

    public Integer getReduceInventoryFlag() {
        return reduceInventoryFlag;
    }

    public void setReduceInventoryFlag(Integer reduceInventoryFlag) {
        this.reduceInventoryFlag = reduceInventoryFlag;
    }

    public Integer getPutAwayType() {
        return putAwayType;
    }

    public void setPutAwayType(Integer putAwayType) {
        this.putAwayType = putAwayType;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Integer getSeckill() {
        return seckill;
    }

    public void setSeckill(Integer seckill) {
        this.seckill = seckill;
    }

    public Integer getIsPost() {
        return isPost;
    }

    public void setIsPost(Integer isPost) {
        this.isPost = isPost;
    }

    public Date getDownShelfTime() {
        return downShelfTime;
    }

    public void setDownShelfTime(Date downShelfTime) {
        this.downShelfTime = downShelfTime;
    }

    public Date getUpShelfTime() {
        return upShelfTime;
    }

    public void setUpShelfTime(Date upShelfTime) {
        this.upShelfTime = upShelfTime;
    }

    public String getCommodityDetail() {
        return commodityDetail;
    }

    public void setCommodityDetail(String commodityDetail) {
        this.commodityDetail = commodityDetail;
    }

    public Integer getVirtualSoldNumber() {
        return virtualSoldNumber;
    }

    public void setVirtualSoldNumber(Integer virtualSoldNumber) {
        this.virtualSoldNumber = virtualSoldNumber;
    }

    public Integer getCommoditySource() {
        return commoditySource;
    }

    public void setCommoditySource(Integer commoditySource) {
        this.commoditySource = commoditySource;
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

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FissionGoodsImageDto> getHeadImages() {
        return headImages;
    }

    public void setHeadImages(List<FissionGoodsImageDto> headImages) {
        this.headImages = headImages;
    }

    public List<FissionGoodsImageDto> getDescpritionImages() {
        return descpritionImages;
    }

    public void setDescpritionImages(List<FissionGoodsImageDto> descpritionImages) {
        this.descpritionImages = descpritionImages;
    }

    public Integer getIsHelper() {
        return isHelper;
    }

    public void setIsHelper(Integer isHelper) {
        this.isHelper = isHelper;
    }

    public Integer getHelperNum() {
        return helperNum;
    }

    public void setHelperNum(Integer helperNum) {
        this.helperNum = helperNum;
    }

    public Integer getUpShelf() {
        return upShelf;
    }

    public void setUpShelf(Integer upShelf) {
        this.upShelf = upShelf;
    }

    public Integer getDownShelf() {
        return downShelf;
    }

    public void setDownShelf(Integer downShelf) {
        this.downShelf = downShelf;
    }

    public Integer getSoldNumber() {
        return soldNumber;
    }

    public void setSoldNumber(Integer soldNumber) {
        this.soldNumber = soldNumber;
    }

    public Integer getTotalSoldNumber() {
        return totalSoldNumber;
    }

    public void setTotalSoldNumber(Integer totalSoldNumber) {
        this.totalSoldNumber = totalSoldNumber;
    }

    public Integer getNonPay() {
        return nonPay;
    }

    public void setNonPay(Integer nonPay) {
        this.nonPay = nonPay;
    }

    public List<FissionBrandDto> getBrandDtoList() {
        return brandDtoList;
    }

    public void setBrandDtoList(List<FissionBrandDto> brandDtoList) {
        this.brandDtoList = brandDtoList;
    }

    public List<FissionStyleDto> getStyleDtoList() {
        return styleDtoList;
    }

    public void setStyleDtoList(List<FissionStyleDto> styleDtoList) {
        this.styleDtoList = styleDtoList;
    }
}
