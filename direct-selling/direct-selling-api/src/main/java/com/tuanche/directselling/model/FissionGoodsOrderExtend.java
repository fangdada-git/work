package com.tuanche.directselling.model;

import java.io.Serializable;

public class FissionGoodsOrderExtend implements Serializable {

    private static final long serialVersionUID = -1413542754843908532L;
    /** 
     * 收货人名称
     * @param
     * @return
     */
    private String receiverName;
    private String receiverPhone;

    /** 
     *省份id
     * @param
     * @return
     */
    private Integer provinceId;

    /** 
     *省份名称
     * @param
     * @return
     */
    private String provinceName;

    /** 
     *城市ID
     * @param
     * @return
     */
    private Integer cityId;

    /** 
     *城市名称
     * @param
     * @return
     */
    private String cityName;

    /** 
     * 区县ID
     * @param
     * @return
     */
    private Integer areaId;

    /** 
     *区县名称
     * @param
     * @return
     */
    private String areaName;

    /** 
     *详细地址
     * @param
     * @return
     */
    private String address;

    /** 
     *备注
     * @param
     * @return
     */
    private String memo;

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }
}
