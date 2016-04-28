package com.mushroom.hui.common.cache.exception;

/**
 * Created by yihui on 16/4/1.
 */
public class JedisException extends Exception {
    public JedisException(String msg) {
        super(msg);
    }

    public JedisException(String msg, Exception e) {
        super(msg, e);
    }
}
