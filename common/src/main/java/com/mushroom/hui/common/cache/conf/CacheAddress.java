package com.mushroom.hui.common.cache.conf;

import org.apache.commons.lang.StringUtils;

/**
 * Created by yihui on 16/4/3.
 */
public class CacheAddress {
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

    public CacheAddress(String conf) {
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
