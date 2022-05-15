package com.tuanche.web.vo;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 代理人
 */
public class AfterSaleAgentEditVo implements Serializable {

    private Integer id;
    /**
     * 姓名
     */
    @NotNull(message = "姓名不可为空")
    private String name;

    /**
     * 电话
     */
    @NotNull(message = "电话不可为空")
    private String phone;

    /**
     * 职位
     */
    @NotNull(message = "职位不可为空")
    private String position;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}