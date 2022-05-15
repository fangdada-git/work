package com.tuanche.web.vo;

import java.util.List;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2020/11/11 15:37
 **/
public class CityVo {
    /**
     * 城市ID
     */
    private Integer id;
    /**
     * 城市名称
     */
    private String name;

    /**
     * 节点是否可选中
     */
    private boolean disabled;
    /**
     * 下级城市
     */
    private List<CityVo> children;

    public CityVo(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public CityVo(Integer id, String name, List<CityVo> children) {
        this.id = id;
        this.name = name;
        this.children = children;
    }

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

    public List<CityVo> getChildren() {
        return children;
    }

    public void setChildren(List<CityVo> children) {
        this.children = children;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
