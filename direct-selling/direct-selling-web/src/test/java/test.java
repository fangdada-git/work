import org.junit.Test;

import java.math.BigDecimal;

/**
 * @class: test
 * @description:
 * @author: gongbo
 * @create: 2020年3月4日18:58:32
 */
public class test {

    @Test
    public void test01(){
        BigDecimal waitWithdrawalAmount = BigDecimal.ZERO;
        System.out.println(waitWithdrawalAmount);
        waitWithdrawalAmount = waitWithdrawalAmount.add(new BigDecimal("120.23"));
        Integer res = Integer.parseInt(waitWithdrawalAmount.multiply(BigDecimal.valueOf(100)).setScale(0,BigDecimal.ROUND_DOWN).toString());
        System.out.println(res);

        waitWithdrawalAmount = waitWithdrawalAmount.add(BigDecimal.valueOf(0.33));
        res = Integer.parseInt(waitWithdrawalAmount.setScale(0,BigDecimal.ROUND_DOWN).toString());
        System.out.println(res);

    }
}
