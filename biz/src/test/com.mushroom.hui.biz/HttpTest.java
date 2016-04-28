package com.mushroom.hui.biz;

import com.mushroom.hui.biz.tools.HttpApiUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by yihui on 16/2/2.
 */
public class HttpTest {

    HttpApiUtil httpApiUtil;

    @Before
    public void init() {
        if (null == httpApiUtil) {
            httpApiUtil = new HttpApiUtil();
        }
    }

    @Test
    public void simpleTest() {
        try {
            String ans = httpApiUtil.getHTML();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
