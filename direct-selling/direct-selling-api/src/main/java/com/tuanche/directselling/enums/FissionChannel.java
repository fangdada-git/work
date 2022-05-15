package com.tuanche.directselling.enums;

/**
 * 裂变渠道
 * @author HuangHao
 * @CreatTime 2021-04-09 10:29
 * @param null
 * @return
 */
public enum FissionChannel {

    CHANNEL_1(1, "裸连接"),
    CHANNEL_2(2, "销售渠道"),
    CHANNEL_3(3, "用户运营渠道");

    private String name;
    private Integer channel;

    FissionChannel(Integer channel, String name) {
        this.name = name;
        this.channel = channel;
    }

    public Integer getChannel() {
        return this.channel;
    }

    public String getName() {
        return this.name;
    }

}
