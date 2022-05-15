package com.tuanche.directselling.vo;

import java.io.Serializable;

/**
 * @Author lvsen
 * @Description
 * @Date 2021/7/29
 **/
public class CreatePosterVo implements Serializable {

    private String shareUrl;

    private String posterUrl;

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
