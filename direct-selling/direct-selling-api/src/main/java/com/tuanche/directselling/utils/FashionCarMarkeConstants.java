package com.tuanche.directselling.utils;

public class FashionCarMarkeConstants {

    //助力成功信息是否购买商品
    public enum HelperBuyFlag {
        HAVE_PURCHASED(1, "已购买"),
        HAVE_NOT_PURCHASED(0, "未购买");
        private Integer code;
        private String key;
        HelperBuyFlag(Integer code, String key) {
            this.code = code;
            this.key = key;
        }
        public Integer getCode() {return code;}
        public String getKey() {return this.key;}
    }
    public enum GrantWinNumFlag {
        GRANT(1, "已发放"),
        HNOT_GRANT(2, "未发放");
        private Integer code;
        private String key;
        GrantWinNumFlag(Integer code, String key) {
            this.code = code;
            this.key = key;
        }
        public Integer getCode() {return code;}
        public String getKey() {return this.key;}
    }
    //助力类型
    public enum HelperType {
        SEMIVALENT_CAR(1, "半价车"),
        CAR_COUPON(2, "抵扣券"),
        RED_PACKET(3, "现金红包");
        private Integer code;
        private String key;
        HelperType(Integer code, String key) {
            this.code = code;
            this.key = key;
        }
        public Integer getCode() {return code;}
        public String getKey() {return this.key;}
    }

}
