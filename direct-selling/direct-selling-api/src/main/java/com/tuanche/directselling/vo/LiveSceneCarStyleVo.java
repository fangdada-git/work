package com.tuanche.directselling.vo;

import java.io.Serializable;

/**
 * @Author GongBo
 * @Description 场次活动车型Vo
 * @Date 2020年7月7日16:14:43
 **/
public class LiveSceneCarStyleVo implements Serializable {
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
