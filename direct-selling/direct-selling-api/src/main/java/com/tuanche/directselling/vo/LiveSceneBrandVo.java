package com.tuanche.directselling.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: czy
 * @Date: 2020/4/30 10:34
 * @Version 1.0
 */
public class LiveSceneBrandVo implements Serializable {
    private Integer id;
    private String name;
    private String pinyin;
    private List<LiveSceneBrandCarStyleVo> list;

    public LiveSceneBrandVo() {
    }

    public LiveSceneBrandVo(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public List<LiveSceneBrandCarStyleVo> getList() {
        return list;
    }

    public void setList(List<LiveSceneBrandCarStyleVo> list) {
        this.list = list;
    }
}
