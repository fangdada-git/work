package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.LiveDealerBroadcast;

import java.io.Serializable;

/**
 * @class: LiveInfoDto
 * @description: 直播信息dto
 * @author: dudg
 * @create: 2020-04-29 17:07
 */
public class LiveInfoDto extends LiveDealerBroadcast implements Serializable {

    /**
     * 主播昵称
     */
    private String anchorNick;


    public String getAnchorNick() {
        return anchorNick;
    }

    public void setAnchorNick(String anchorNick) {
        this.anchorNick = anchorNick;
    }

}
