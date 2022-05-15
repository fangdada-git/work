package com.tuanche.directselling.enums;

/**
 * @author：HuangHao
 * @CreatTime 2021-03-09 15:09
 */
public enum BrankAccountType {
    //银行账户类型
    ACCOUNT_TYPE_1(1,"对公账户"),
    ACCOUNT_TYPE_2(2,"个人账户");

    private Integer code;
    private String text;

    private BrankAccountType(Integer code,String text){
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
        for (BrankAccountType t : BrankAccountType.values()) {
            if (code.equals(t.getCode())) {
                return t.text;
            }
        }
        return null;
    }
}
