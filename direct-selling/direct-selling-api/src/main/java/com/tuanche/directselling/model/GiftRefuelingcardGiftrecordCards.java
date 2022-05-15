package com.tuanche.directselling.model;

import java.io.Serializable;

public class GiftRefuelingcardGiftrecordCards implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 油卡赠送记录ID
     */
    private Integer giftrecordId;

    /**
     * 赠品-油卡卡号
     */
    private String refuelingCode;

    /**
     * gift_refuelingcard_giftrecord_cards
     */
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGiftrecordId() {
        return giftrecordId;
    }

    public void setGiftrecordId(Integer giftrecordId) {
        this.giftrecordId = giftrecordId;
    }

    public String getRefuelingCode() {
        return refuelingCode;
    }

    public void setRefuelingCode(String refuelingCode) {
        this.refuelingCode = refuelingCode;
    }
}