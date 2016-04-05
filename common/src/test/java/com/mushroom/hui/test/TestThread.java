package com.mushroom.hui.test;

import com.mushroom.hui.common.cache.impl.MySubscribe;
import com.mushroom.hui.common.cache.impl.CacheService;

/**
 * Created by yihui on 16/4/4.
 */
public class TestThread extends Thread {
    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TestThread.class);

    private CacheService wrapper;

    public TestThread(CacheService wrapper){
        log.info("loading test thread");
        this.wrapper = wrapper;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        wrapper.psubscribe(new MySubscribe(), "*");
        log.info("over!");
    }
}