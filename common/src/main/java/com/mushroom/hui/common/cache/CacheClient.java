package com.mushroom.hui.common.cache;

import com.mushroom.hui.common.cache.api.CacheInterface;
import com.mushroom.hui.common.register.BaseRegister;
import com.mushroom.hui.common.register.callback.CacheCallBackInterface;
import com.mushroom.hui.common.register.exception.RegisterException;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;

/**
 * Created by yihui on 16/4/5.
 */
public class CacheClient implements BaseRegister {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CacheClient.class);

    @Resource(name = "cacheService")
    private CacheInterface methodCache;


    @Override
    public void register(String name, CacheCallBackInterface callback) throws Exception {
        if(StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("key is empty!");
        }

        if (map.containsKey(name)) {
            throw new RegisterException("this callback interface has already registered! name : " + name);
        }

        map.put(name, callback);
    }


    @Override
    public Object exec(String name, int id) throws Exception {
        return exec(name, id, Object.class);
    }


    @Override
    public <T> T exec(String name, int id, Class<T> clz) throws Exception {
        CacheCallBackInterface callBackInterface = map.get(name);
        if (callBackInterface == null) {
            throw new RegisterException("this callback interface has already registered! name : " + name);
        }

        String key = callBackInterface.getKey(id);
        Object obj = methodCache.getObject(key, clz);
        if (obj == null) {
            obj = callBackInterface.getObject(key);
            methodCache.setObject(key, obj, callBackInterface.getExpire());
        }

        return (T) obj;
    }


    /**
     * 获取缓存的对象
     * @param name  注册的回调函数name
     * @param id    id
     * @param clz   返回的对象类型
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T getObject(String name, int id, Class<T> clz) throws Exception {
        return exec(name, id, clz);
    }

}
