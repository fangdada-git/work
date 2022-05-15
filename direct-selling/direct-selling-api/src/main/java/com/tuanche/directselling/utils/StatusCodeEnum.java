package com.tuanche.directselling.utils;

public enum StatusCodeEnum {

    /* 成功状态码 */
    SUCCESS(200, "成功"),
    ERROR(500, "系统错误"),

    /*参数错误 10001-19999 */
    PARAM_IS_INVALID(10001, "参数无效"),
    PARAM_IS_BLANK(10002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(10003, "参数类型错误"),
    PARAM_NOT_COMPLETE(10004, "参数缺失"),

    /* 用户错误：20001-29999*/
    USER_NOT_LOGGED_IN(20001, "用户未登录"),
    USER_LOGIN_ERROR(20002, "账号不存在或密码错误"),
    USER_ACCOUNT_FORBIDDEN(20003, "账号已被禁用"),
    USER_NOT_EXIST(20004, "用户不存在"),
    USER_HAS_EXISTED(20005, "用户已存在"),
    Cert_HAS_EXISTED(20006, "认证已存在"),


    /* 业务错误：30001-39999 */
    CREATE_FAIL(30001, "创建失败"),
    PROGRAM_TIME_ERROR(30002, "节目时间冲突，请修改"),
    NO_ACCESS(30003, "禁止访问"),
    UNION_ID_EXIST(30004, "您已是代理人"),
    COUPON_ISSUED(30005, "券码已发放"),
    WIN_NUM_CREATE_ERROR(30006, "生成中奖码错误"),
    WIN_NUM_EXPIRE_ERROR(30007, "活动结束"),
    WIN_NUM_ENOUGH_ERROR(30008, "获得的中奖码已到上限"),
    WIN_NUM_NO_USER_ERROR(30009, "没有获得userId"),
    WIN_NUM_NO_PERIODS_ERROR(30010, "没有场次数据"),
    WIN_NUM_CREATE_TIMEOUT_ERROR(30011, "生成中奖码超时"),

    /* 系统错误：40001-49999 */
    SYSTEM_INNER_ERROR(40001, "系统繁忙，请稍后重试"),

    /* 数据错误：50001-599999 */
    DATA_IS_WRONG(50002, "数据有误"),
    RESULE_DATA_NONE(50001, "数据未找到"),
    DATA_ALREADY_EXISTED(50003, "数据已存在"),
    PIC_VERIFY_CODE_ERROR(50004, "图片验证码错误"),
    PHONE_VERIFY_CODE_ERROR(50005, "手机号验证码错误"),

    /* 接口错误：60001-69999 */
    INTERFACE_INNER_INVOKE_ERROR(60001, "内部系统接口调用异常"),
    INTERFACE_OUTTER_INVOKE_ERROR(60002, "外部系统接口调用异常"),
    INTERFACE_FORBID_VISIT(60003, "该接口禁止访问"),
    INTERFACE_ADDRESS_INVALID(60004, "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT(60005, "接口请求超时"),
    INTERFACE_EXCEED_LOAD(60006, "接口负载过高"),

    /* 权限错误：70001-79999 */
    PERMISSION_NO_ACCESS(70001, "只有标签 Owner ,才具备删除权限"),
    PERMISSION_NO_PHONE_ACCESS(70002,"此认证标签已有员工认证，不可以进行删除");
	
    private int code;
    private String text;

    StatusCodeEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }

    // 普通方法
    public static String getText(int code) {
        for (StatusCodeEnum t : StatusCodeEnum.values()) {
            if (t.getCode() == code) {
                return t.getText();
            }
        }
        return "待补充";
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
