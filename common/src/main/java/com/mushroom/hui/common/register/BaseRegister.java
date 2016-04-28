package com.mushroom.hui.common.register;


import com.mushroom.hui.common.register.callback.CacheCallBackInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yihui on 16/4/5.
 */
public interface BaseRegister {
    Map<String, CacheCallBackInterface> map = new HashMap<>();

    void register(String name, CacheCallBackInterface callback) throws Exception;

    Object exec(String name, int id) throws Exception;

    <T> T exec(String name, int id, Class<T> clz) throws Exception;
}
