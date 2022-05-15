package com.tuanche.directselling.dto;

import java.io.Serializable;

public class FissionGoodsImageDto implements Serializable {

    private static final long serialVersionUID = 9020136491714282375L;
    /**
     * 图片文件流
     */
    private String path;
    /**
     * 展示序号
     */
    private Integer orderNumber;
    /**
     * 图片描述
     */
    private String description;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

}
