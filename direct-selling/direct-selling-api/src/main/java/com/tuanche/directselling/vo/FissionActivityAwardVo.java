package com.tuanche.directselling.vo;/**
 * @program: direct-selling
 * @description: 裂变活动奖励vo
 * @author: lvsen
 * @create: 2020-09-30 13:59
 **/

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author lvsen
 * @Description
 * @Date 2020/9/30
 **/
public class FissionActivityAwardVo implements Serializable {

    private static final long serialVersionUID = -3283818498114858800L;
    /**
     * 裂变id
     */
    private Integer fissionId;
    /**
     * 规则类型(1B端 2C端)
     */
    private Short ruleType;
    /**
     * 奖金池金额
     */
    private BigDecimal prizePool;
    /**
     * 单用户上限/对赌金
     */
    private BigDecimal personMoney;
    /**
     * 任务类型code(对应fission_dict任务code)
     */
    private String taskCode;
    /**
     * 浏览奖励
     */
    private BigDecimal browseAward;
    /**
     * 浏览奖励规则
     */
    private BigDecimal browseAwardRule;
    /**
     * 预约奖励
     */
    private BigDecimal subscribeAward;
    /**
     * 预约奖励规则
     */
    private BigDecimal subscribeAwardRule;
    /**
     * 购买活动商品奖励
     */
    private BigDecimal buyActivityAward;
    /**
     * 购买活动商品奖励规则
     */
    private BigDecimal buyActivityAwardRule;
    /**
     * 观看奖励
     */
    private BigDecimal watchAward;
    /**
     * 观看奖励规则
     */
    private BigDecimal watchAwardRule;
    /**
     * 购买直播商品奖励
     */
    private BigDecimal buyBroadcastAward;
    /**
     * 购买直播商品奖励规则
     */
    private BigDecimal buyBroadcastAwardRule;

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
    }

    public BigDecimal getPrizePool() {
        return prizePool;
    }

    public void setPrizePool(BigDecimal prizePool) {
        this.prizePool = prizePool;
    }

    public BigDecimal getPersonMoney() {
        return personMoney;
    }

    public void setPersonMoney(BigDecimal personMoney) {
        this.personMoney = personMoney;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public BigDecimal getBrowseAward() {
        return browseAward;
    }

    public void setBrowseAward(BigDecimal browseAward) {
        this.browseAward = browseAward;
    }

    public BigDecimal getBrowseAwardRule() {
        return browseAwardRule;
    }

    public void setBrowseAwardRule(BigDecimal browseAwardRule) {
        this.browseAwardRule = browseAwardRule;
    }

    public BigDecimal getSubscribeAward() {
        return subscribeAward;
    }

    public void setSubscribeAward(BigDecimal subscribeAward) {
        this.subscribeAward = subscribeAward;
    }

    public BigDecimal getSubscribeAwardRule() {
        return subscribeAwardRule;
    }

    public void setSubscribeAwardRule(BigDecimal subscribeAwardRule) {
        this.subscribeAwardRule = subscribeAwardRule;
    }

    public BigDecimal getBuyActivityAward() {
        return buyActivityAward;
    }

    public void setBuyActivityAward(BigDecimal buyActivityAward) {
        this.buyActivityAward = buyActivityAward;
    }

    public BigDecimal getBuyActivityAwardRule() {
        return buyActivityAwardRule;
    }

    public void setBuyActivityAwardRule(BigDecimal buyActivityAwardRule) {
        this.buyActivityAwardRule = buyActivityAwardRule;
    }

    public BigDecimal getWatchAward() {
        return watchAward;
    }

    public void setWatchAward(BigDecimal watchAward) {
        this.watchAward = watchAward;
    }

    public BigDecimal getWatchAwardRule() {
        return watchAwardRule;
    }

    public void setWatchAwardRule(BigDecimal watchAwardRule) {
        this.watchAwardRule = watchAwardRule;
    }

    public BigDecimal getBuyBroadcastAward() {
        return buyBroadcastAward;
    }

    public void setBuyBroadcastAward(BigDecimal buyBroadcastAward) {
        this.buyBroadcastAward = buyBroadcastAward;
    }

    public BigDecimal getBuyBroadcastAwardRule() {
        return buyBroadcastAwardRule;
    }

    public void setBuyBroadcastAwardRule(BigDecimal buyBroadcastAwardRule) {
        this.buyBroadcastAwardRule = buyBroadcastAwardRule;
    }

    public Short getRuleType() {
        return ruleType;
    }

    public void setRuleType(Short ruleType) {
        this.ruleType = ruleType;
    }
}
