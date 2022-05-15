package com.tuanche.directselling.dto;

import java.io.Serializable;

public class FissionStyleDto implements Serializable {

    private Integer styleId;
    private String styleName;

    public FissionStyleDto (){}
    public FissionStyleDto (Integer styleId, String styleName){
        this.styleId = styleId;
        this.styleName = styleName;
    }

    public Integer getStyleId() {
        return styleId;
    }

    public void setStyleId(Integer styleId) {
        this.styleId = styleId;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

}
