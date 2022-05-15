package com.tuanche.directselling.pojo;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/10/18 17:41
 **/
public class CommonHeaderVo {
    /**
     * 返回个数
     */
    private Integer size = 10;
    /**
     * 起始位置
     */
    private Integer startPos = 0;
    /**
     * 是否随机返回
     */
    private boolean random = false;
    /**
     * 0顺序 非0倒序
     */
    private Integer sort = 0;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getStartPos() {
        return startPos;
    }

    public void setStartPos(Integer startPos) {
        this.startPos = startPos;
    }

    public boolean isRandom() {
        return random;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
