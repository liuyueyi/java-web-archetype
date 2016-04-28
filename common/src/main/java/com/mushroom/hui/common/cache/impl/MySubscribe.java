package com.mushroom.hui.common.cache.impl;

import redis.clients.jedis.JedisPubSub;

/**
 * Created by yihui on 16/4/4.
 */
public class MySubscribe extends JedisPubSub {

    private static final org.slf4j.Logger log= org.slf4j.LoggerFactory.getLogger(MySubscribe.class);

    // 初始化按表达式的方式订阅时候的处理
    public void onPSubscribe(String pattern, int subscribedChannels) {
        log.info(pattern + "=" + subscribedChannels);
    }

    // 取得按表达式的方式订阅的消息后的处理
    public void onPMessage(String pattern, String channel, String message) {
        log.info(pattern + "=" + channel + "=" + message);
    }
}