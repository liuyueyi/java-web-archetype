package com.mushroom.hui.common.cache;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yihui on 16/4/1.
 */
public class CacheWrapper implements InitializingBean {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CacheWrapper.class);

    private JedisPool masterJedis;
    private List<JedisPool> slaveJedisList;

    private String masterConf;
    private String slaveConf;
    private int maxIdle;
    private int minIdle;
    private int maxTotal;
    private int timeout;
    private int database;

    public String getMasterConf() {
        return masterConf;
    }

    public void setMasterConf(String masterConf) {
        this.masterConf = masterConf;
    }

    public String getSlaveConf() {
        return slaveConf;
    }

    public void setSlaveConf(String slaveConf) {
        this.slaveConf = slaveConf;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    private class ConfAddress {
        private String ip;
        private int port;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public ConfAddress(String conf) {
            init(conf);
        }

        private void init(String conf) {
            if (!StringUtils.contains(conf, ":")) {
                return;
            }

            String[] pair = StringUtils.split(conf, ":");
            if (pair == null || pair.length != 2) {
                return;
            }

            this.ip = pair[0];
            this.port = Integer.parseInt(pair[1]);
        }

        public boolean isIllegal() {
            return StringUtils.isBlank(ip) || port <= 0;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal <= 0 ? 300 : maxTotal);
        config.setMaxIdle(maxIdle <= 0 ? 10 : maxIdle);
        config.setMinIdle(minIdle <= 0 ? 3 : minIdle);
        config.setMaxWaitMillis(timeout <= 0 ? 1000 : timeout);
        config.setTestOnBorrow(false);


        // init master jedis
        ConfAddress masterAddr = new ConfAddress(masterConf);
        if (masterAddr.isIllegal()) {
            throw new JedisException("master jedis conf is error!");
        }
        masterJedis = new JedisPool(config, masterAddr.getIp(), masterAddr.getPort(), this.timeout, null, this.database);


        // init slave jedis
        String[] slaveConfs = StringUtils.split(slaveConf, ",");
        if (slaveConfs == null || slaveConfs.length == 0) {
            slaveJedisList = Collections.emptyList();
        }
        slaveJedisList = new ArrayList<>(slaveConfs.length);
        ConfAddress slaveTmpAddr;
        for (String conf: slaveConfs) {
            slaveTmpAddr = new ConfAddress(conf);
            if(slaveTmpAddr.isIllegal()) {
                continue;
            }
            JedisPool slaveJedis = new JedisPool(config, slaveTmpAddr.getIp(), slaveTmpAddr.getPort(),
                    this.timeout, null, this.database);
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

}
