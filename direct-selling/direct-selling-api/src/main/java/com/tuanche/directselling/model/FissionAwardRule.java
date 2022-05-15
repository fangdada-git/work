package com.tuanche.directselling.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description 裂变奖励规则
 * @date 2020/9/22 16:35
 * @author lvsen
 */
public class FissionAwardRule implements Serializable {

    private static final long serialVersionUID = 6976728548640689612L;
    private Integer id;
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
     * 奖励
     */
    private BigDecimal award;
    /**
     * 奖励规则(c端上限 b端基础目标)
     */
    private BigDecimal awardRule;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
    }

    public Short getRuleType() {
        return ruleType;
    }

    public void setRuleType(Short ruleType) {
        this.ruleType = ruleType;
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

    public BigDecimal getAward() {
        return award;
    }

    public void setAward(BigDecimal award) {
        this.award = award;
    }

    public BigDecimal getAwardRule() {
        return awardRule;
    }

    public void setAwardRule(BigDecimal awardRule) {
        this.awardRule = awardRule;
    }
}