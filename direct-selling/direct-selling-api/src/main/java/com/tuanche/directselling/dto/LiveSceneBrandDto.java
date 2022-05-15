package com.tuanche.directselling.dto;

import java.io.Serializable;

/**
 * @ClassName: LiveSceneListDto
 * @Description: 场次活动城市dto
 * @Author: GongBo
 * @Date: 2020/4/2 11:46
 * @Version 1.0
 **/
public class LiveSceneBrandDto implements Serializable {

    private Integer id;

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
