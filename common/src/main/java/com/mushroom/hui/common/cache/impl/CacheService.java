package com.mushroom.hui.common.cache.impl;

import com.alibaba.fastjson.JSON;
import com.mushroom.hui.common.cache.api.CacheInterface;
import com.mushroom.hui.common.cache.conf.CacheAddress;
import com.mushroom.hui.common.cache.conf.CacheConf;
import com.mushroom.hui.common.cache.exception.JedisException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yihui on 16/4/1.
 */
public class CacheService implements InitializingBean, CacheInterface {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CacheService.class);

    private JedisPool masterJedis;
    private List<JedisPool> slaveJedisList;

    // redis 配置信息
    @Autowired
    private CacheConf cacheConf;

    public CacheConf getCacheConf() {
        return cacheConf;
    }

    public void setCacheConf(CacheConf cacheConf) {
        this.cacheConf = cacheConf;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(cacheConf.getMaxTotal() <= 0 ? 300 : cacheConf.getMaxTotal());
        config.setMaxIdle(cacheConf.getMaxIdle() <= 0 ? 10 : cacheConf.getMaxIdle());
        config.setMinIdle(cacheConf.getMinIdle() <= 0 ? 3 : cacheConf.getMinIdle());
        config.setMaxWaitMillis(cacheConf.getTimeout() <= 0 ? 1000 : cacheConf.getTimeout());
        config.setTestOnBorrow(false);


        // init master jedis
        CacheAddress masterAddr = new CacheAddress(cacheConf.getMasterConf());
        if (masterAddr.isIllegal()) {
            throw new JedisException("master jedis conf is error!");
        }
        masterJedis = new JedisPool(config, masterAddr.getIp(), masterAddr.getPort(), cacheConf.getTimeout(), null, cacheConf.getDatabase());


        // init slave jedis
        String[] slaveConfs = StringUtils.split(cacheConf.getSlaveConf(), ",");
        if (slaveConfs == null || slaveConfs.length == 0) {
            slaveJedisList = Collections.emptyList();
        }
        slaveJedisList = new ArrayList<>(slaveConfs.length);
        CacheAddress slaveTmpAddr;
        for (String conf: slaveConfs) {
            slaveTmpAddr = new CacheAddress(conf);
            if(slaveTmpAddr.isIllegal()) {
                continue;
            }
            JedisPool slaveJedis = new JedisPool(config, slaveTmpAddr.getIp(), slaveTmpAddr.getPort(),
                    cacheConf.getTimeout(), null, cacheConf.getDatabase());
            slaveJedisList.add(slaveJedis);
        }
    }



    final int MASTER_JEDIS = 0;
    final int SLAVE_JEIDS = 1;
    // 保证线程安全的自动技术器
    private AtomicInteger chooseCounter = new AtomicInteger();

    /**
     * 获取使用的jedis,这里采用标准的一主多备模式
     * @param type
     * @return
     */
    public JedisPool getJedisPool(int type) {
        if (type == MASTER_JEDIS) {
            return masterJedis;
        }

        if (CollectionUtils.isEmpty(slaveJedisList)) {
            return masterJedis;
        }


        final int chooseIndex = this.chooseCounter.incrementAndGet();
        final int index = chooseIndex % slaveJedisList.size();
        return slaveJedisList.get(index);
    }


    @Override
    public String get(String key) {
        if(StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("key is null!");
        }

        Jedis jedis = null;
        JedisPool pool = getJedisPool(SLAVE_JEIDS);
        try {
            jedis = pool.getResource();
            String ans = jedis.get(key);
            return ans;
        } catch (Exception e) {
            logger.error("get string from cache error!");
            logger.error("Exception: {}", e);
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 采用fastJson作为序列化工具
     * @param key cache key
     * @param clz object class
     * @param <T> type
     * @return object in cache!
     */
    @Override
    public <T> T getObject(String key, Class<T> clz) {
        if(StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("key is null");
        }

        Jedis jedis = null;
        JedisPool pool = getJedisPool(SLAVE_JEIDS);
        try {
            jedis = pool.getResource();
            String ans = jedis.get(key);
            if (StringUtils.isBlank(ans) || "nil".equals(ans)) {
                return null;
            }

            T obj = JSON.parseObject(ans, clz);
            return obj;
        } catch (Exception e) {
            logger.error("get object from cache error!");
            logger.error("Exception: {}", e);
            return null;
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }


    @Override
    public boolean set(String key, String value, int expire) {
        if(StringUtils.isBlank(key) || StringUtils.isBlank(value) || expire <= 0) {
            throw new IllegalArgumentException("key || value || expire are illegal");
        }

        Jedis jedis = null;
        JedisPool pool = getJedisPool(MASTER_JEDIS);
        try {
            jedis = pool.getResource();
            String ans = jedis.setex(key, expire, value);
        } catch (Exception e) {
            logger.error("set string into cache error!");
            logger.error("Exception: {}", e);
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return true;
    }


    @Override
    public boolean setObject(String key, Object value, int expire) {
        if (StringUtils.isBlank(key) || value == null || expire <= 0) {
            throw new IllegalArgumentException("key value expire are illegal!");
        }

        Jedis jedis = null;
        JedisPool pool = getJedisPool(MASTER_JEDIS);
        try {
            jedis = pool.getResource();
            String data = JSON.toJSONString(value);
            jedis.setex(key, expire, data);
        } catch (Exception e) {
            logger.error("set object into cache error!");;
            logger.error("Exception: {}", e);
            return false;
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
        return true;
    }


    /**
     *
     * @param sub
     * @param pattern
     */
    public void psubscribe(JedisPubSub sub, String ...pattern) {
        Jedis jedis = null;
        JedisPool pool = getJedisPool(MASTER_JEDIS);
        try{
            jedis = pool.getResource();
            jedis.psubscribe(sub, pattern);
        } catch (Exception e) {
            logger.error("psubscribe error!");
            logger.error("Exception: {}", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

}
