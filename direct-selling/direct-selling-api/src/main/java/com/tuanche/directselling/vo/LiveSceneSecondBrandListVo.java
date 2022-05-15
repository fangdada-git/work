package com.tuanche.directselling.vo;

import java.io.Serializable;

/**
 * @Author GongBo
 * @Description 直卖二级品牌Vo
 * @Date 14:57 2020/7/7
 **/
public class LiveSceneSecondBrandListVo implements Serializable {
    /**
     * 品牌ID
     **/
    private Integer cbId;

    /**
     * 品牌名称
     **/
    private String cbName;

    /**
     * 品牌logo
     **/
    private String cbLogo;

    public Integer getCbId() {
        return cbId;
    }

    public void setCbId(Integer cbId) {
        this.cbId = cbId;
    }

    public String getCbName() {
        return cbName;
    }

    public void setCbName(String cbName) {
        this.cbName = cbName;
    }

    public String getCbLogo() {
        return cbLogo;
    }

    public void setCbLogo(String cbLogo) {
        this.cbLogo = cbLogo;
    }
}
