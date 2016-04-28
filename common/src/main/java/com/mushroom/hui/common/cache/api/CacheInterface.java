package com.mushroom.hui.common.cache.api;

/**
 * Created by yihui on 16/4/1.
 */
public interface CacheInterface {

    String get(String key);

    <T> T getObject(String key, Class<T> clz);

    boolean set(String key, String value, int expire);

    boolean setObject(String key, Object value, int expire);
}
