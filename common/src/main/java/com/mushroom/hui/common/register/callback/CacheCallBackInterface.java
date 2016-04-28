package com.mushroom.hui.common.register.callback;

/**
 * 缓存未命中的回调函数
 * Created by yihui on 16/4/5.
 */
public interface CacheCallBackInterface {

    String getKey(int id);

    int getExpire();

    Object getObject(String key);
}
