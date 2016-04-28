package com.mushroom.hui.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 权限判断工具类
 * Created by yihui on 16/3/30.
 */
public class AuthorityUtil {
    static long lastUpdated = 0;
    static String resourceName = "/tmp/rate.properties";
    static String absolutePath;
    static Map<Object, Object> map = null;

    static ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    static {
        executor.scheduleAtFixedRate(
                new ConfFileScanner(),
                0,
                30,
                TimeUnit.SECONDS);
    }

    public static Map<Object, Object> getResourceValues() throws IOException {

        if (map == null) {
            Properties properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream(absolutePath);
            properties.load(new InputStreamReader(fileInputStream, "UTF-8"));
            map = properties;
        }

        return map;
    }


}

class ConfFileScanner implements Runnable {

    public void run() {
        try {
            if (isFileUpdated()) {
                Properties properties = new Properties();
                FileInputStream fileInputStream = new FileInputStream(AuthorityUtil.absolutePath);
                properties.load(new InputStreamReader(fileInputStream, "UTF-8"));
                synchronized (AuthorityUtil.class) {
                    AuthorityUtil.map = properties;
                }
                System.out.println("update map");
            }
        } catch (Exception e) {
            System.out.println("update map error ! " + e.getMessage());
        }
    }

    /**
     * 判断配置文件是否更新
     *
     * @return
     */
    private static boolean isFileUpdated() {
        if (AuthorityUtil.resourceName.startsWith("/")) {
            AuthorityUtil.absolutePath = AuthorityUtil.resourceName;
        } else {
            URL path = ClassLoader.getSystemResource(AuthorityUtil.resourceName);
            AuthorityUtil.absolutePath = path.getPath();
        }
        File f = new File(AuthorityUtil.absolutePath);
        long lastTime = f.lastModified();
        if (lastTime != AuthorityUtil.lastUpdated) {
            AuthorityUtil.lastUpdated = lastTime;
            return true;
        } else {
            return false;
        }
    }
}
