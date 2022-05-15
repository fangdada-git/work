package com.tuanche.directselling;

import com.tuanche.directselling.service.impl.LiveDealerAnchorServiceImpl;
import com.tuanche.directselling.vo.LiveDealerAnchorVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.math.BigDecimal;

/**
 * @program: direct-selling
 * @description: ${description}
 * @author: fxq
 * @create: 2020-03-29 17:40
 **/
public class Tese extends ApplicationTest {

    @Autowired
    private LiveDealerAnchorServiceImpl  liveDealerAnchorServiceImpl;

    @Test
    public void test () {
        LiveDealerAnchorVo liveDealerAnchorVo = new LiveDealerAnchorVo();
        liveDealerAnchorVo.setInt_type(3);
        liveDealerAnchorServiceImpl.LiveDealerAnchorRanking(liveDealerAnchorVo);
    }
}
