package com.mushroom.hui.common.cache.conf;

import org.springframework.stereotype.Component;

/**
 * Created by yihui on 16/4/3.
 */
@Component
public class CacheConf {
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

}
