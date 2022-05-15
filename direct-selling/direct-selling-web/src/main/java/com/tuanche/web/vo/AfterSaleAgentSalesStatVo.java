package com.tuanche.web.vo;

import java.io.Serializable;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/7/21 21:12
 **/
public class AfterSaleAgentSalesStatVo implements Serializable {
    /**
     * 姓名
     */
    private String name;
    /**
     * 职位
     */
    private String position;

    /**
     * 总销量
     */
    private Integer total;
    /**
     * 员工销量
     */
    private Integer staffSales;

    /**
     * 转介绍销量
     */
    private Integer forwardSales;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getStaffSales() {
        return staffSales;
    }

    public void setStaffSales(Integer staffSales) {
        this.staffSales = staffSales;
    }

    public Integer getForwardSales() {
        return forwardSales;
    }

    public void setForwardSales(Integer forwardSales) {
        this.forwardSales = forwardSales;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
