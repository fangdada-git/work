package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.GiftRefuelingcardGiftrecord;
import com.tuanche.directselling.utils.GlobalConstants;

import java.io.Serializable;

/**
 * @author：HuangHao
 * @CreatTime 2020-05-12 18:14
 */
public class GiftRefuelingcardGiftrecordDto extends GiftRefuelingcardGiftrecord  implements Serializable {
    private static final long serialVersionUID = 1L;

    //油卡卡号
    private String refuelingCode;

    //订单渠道字符串
    private String orderChannleStr;
    //经销商已赠送油卡数量
    private Integer presentedCardNum;


    //重写setOrderChannel方法
    public void setOrderChannel(String orderChannel) {
        this.orderChannel = orderChannel;
        this.orderChannleStr=GlobalConstants.RefuelingCardOrderChannel.getText(orderChannel);
    }

    public String getRefuelingCode() {
        return refuelingCode;
    }

    public void setRefuelingCode(String refuelingCode) {
        this.refuelingCode =  refuelingCode;
    }
    public String getOrderChannleStr() {
        return orderChannleStr;
    }

    public Integer getPresentedCardNum() {
        return presentedCardNum;
    }

    public void setPresentedCardNum(Integer presentedCardNum) {
        this.presentedCardNum = presentedCardNum;
    }
}
