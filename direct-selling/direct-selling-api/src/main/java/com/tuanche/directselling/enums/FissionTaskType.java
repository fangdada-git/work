package com.tuanche.directselling.enums;

/**
 * 裂变活动任务枚举
 * @author：HuangHao
 * @CreatTime 2020-10-09 18:03
 */
public enum  FissionTaskType {
    //任务
    TASK_1(1,"浏览页面"),
    TASK_2(2,"预约直播"),
    TASK_3(3,"购买活动页商品"),
    TASK_4(4,"观看直播"),
    TASK_5(5,"购买直播商品");

    private Integer code;
    private String text;

    private FissionTaskType(Integer code,String text){
        this.code = code;
        this.text = text;
    }
    public Integer getCode(){
        return code;
    }
    public static String getText(Integer code) {
        if(code==null){
            return null;
        }
        for (FissionTaskType t : FissionTaskType.values()) {
            if (code.equals(t.getCode())) {
                return t.text;
            }
        }
        return null;
    }
}
