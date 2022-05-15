package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.AfterSaleActivityChannelStatistics;
import com.tuanche.directselling.utils.GlobalConstants;

import java.io.Serializable;
import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2021-09-15 18:04
 */
public class AfterSaleActivityChannelStatisticsDto extends AfterSaleActivityChannelStatistics implements Serializable {

    //渠道名称
    private String channelName;
    //推广卡售卖占比
    private String promotionCardsSellPercent;
    //渠道总退款数
    private int promotionCardRefundsTotal;
    //套餐转化率
    private String packagePercent;

    public String getPromotionCardsSellPercent() {
        return promotionCardsSellPercent;
    }

    public void setPromotionCardsSellPercent(String promotionCardsSellPercent) {
        this.promotionCardsSellPercent = promotionCardsSellPercent;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getPromotionCardRefundsTotal() {
        return promotionCardRefundsTotal;
    }

    public void setPromotionCardRefundsTotal(int promotionCardRefundsTotal) {
        this.promotionCardRefundsTotal = promotionCardRefundsTotal;
    }

    public String getPackagePercent() {
        return packagePercent;
    }

    public void setPackagePercent(String packagePercent) {
        this.packagePercent = packagePercent;
    }
}
