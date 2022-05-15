package com.tuanche.directselling.vo;

import java.io.Serializable;

/**
 * @Author: czy
 * @Date: 2020/4/22 15:43
 * @Version 1.0
 */
public class LiveSceneBrandListVo implements Serializable {
    //一级品牌id
    private Integer cmId;
    //一级品牌名称
    private String cmName;
    //英文名称
    private String cmEnName;
    //首字母
    private String cmInitial;
    //品牌logo
    private String cmLogo;

    public Integer getCmId() {
        return cmId;
    }

    public void setCmId(Integer cmId) {
        this.cmId = cmId;
    }

    public String getCmName() {
        return cmName;
    }

    public void setCmName(String cmName) {
        this.cmName = cmName;
    }

    public String getCmEnName() {
        return cmEnName;
    }

    public void setCmEnName(String cmEnName) {
        this.cmEnName = cmEnName;
    }

    public String getCmInitial() {
        return cmInitial;
    }

    public void setCmInitial(String cmInitial) {
        this.cmInitial = cmInitial;
    }

    public String getCmLogo() {
        return cmLogo;
    }

    public void setCmLogo(String cmLogo) {
        this.cmLogo = cmLogo;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LiveSceneBrandListVo other = (LiveSceneBrandListVo) obj;
        if (other.getCmId() == null) {
            return false;
        }
        if (cmId == null) {
            if (other.cmId != null)
                return false;
        } else if (!cmId.equals(other.cmId))
            return false;
        return true;
    }
}
