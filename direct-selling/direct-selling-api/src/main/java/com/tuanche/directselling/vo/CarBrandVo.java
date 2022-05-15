package com.tuanche.directselling.vo;

import java.io.Serializable;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2020/10/28 13:53
 **/
public class CarBrandVo implements Serializable {

    /**
     * 品牌ID
     */
    private Integer id;

    /**
     * 品牌名称
     */
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
