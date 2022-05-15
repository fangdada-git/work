package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.GiftRefuelingcardPeriods;
import com.tuanche.directselling.model.GiftRefuelingcardPeriodsCommodity;
import com.tuanche.directselling.model.GiftRefuelingcardPeriodsGiftNum;

import java.io.Serializable;
import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2020-05-27 16:03
 */
public class GiftRefuelingcardPeriodsDto extends GiftRefuelingcardPeriods{

    //商品类型，例：代金券,一口价
    private String commodityNames;
    //油卡规则，例：99.00元1张油卡,499.00元5张油卡
    private String fuelCardRules;
    //油卡商品类型列表
    private List<GiftRefuelingcardPeriodsCommodity> commodityList;
    //油卡配置规则列表
    private List<GiftRefuelingcardPeriodsGiftNum> fuelCardRuleList;

    public String getCommodityNames() {
        return commodityNames;
    }

    public void setCommodityNames(String commodityNames) {
        this.commodityNames = commodityNames;
    }

    public String getFuelCardRules() {
        return fuelCardRules;
    }

    public void setFuelCardRules(String fuelCardRules) {
        this.fuelCardRules = fuelCardRules;
    }

    public List<GiftRefuelingcardPeriodsCommodity> getCommodityList() {
        return commodityList;
    }

    public void setCommodityList(List<GiftRefuelingcardPeriodsCommodity> commodityList) {
        this.commodityList = commodityList;
    }

    public List<GiftRefuelingcardPeriodsGiftNum> getFuelCardRuleList() {
        return fuelCardRuleList;
    }

    public void setFuelCardRuleList(List<GiftRefuelingcardPeriodsGiftNum> fuelCardRuleList) {
        this.fuelCardRuleList = fuelCardRuleList;
    }
}
