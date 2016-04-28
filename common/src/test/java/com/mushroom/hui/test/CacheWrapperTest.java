package com.mushroom.hui.test;

import com.mushroom.hui.common.cache.CacheClient;
import com.mushroom.hui.common.cache.impl.CacheService;
import com.mushroom.hui.common.register.callback.CacheCallBackInterface;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yihui on 16/4/2.
 */
public class CacheWrapperTest {

    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CacheWrapperTest.class);

    private CacheService cacheService;
    private CacheClient cacheClient;

    @Before
    public void init() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:spring/*.xml");
        cacheService = applicationContext.getBean("cacheService", CacheService.class);
        cacheClient = applicationContext.getBean("cacheClient", CacheClient.class);
    }

    private class Point {
        String name;
        Float x;
        float y;

        public Point(String name, Float x, float y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Float getX() {
            return x;
        }

        public void setX(Float x) {
            this.x = x;
        }
    }

    @Test
    public void cacheTest() {
        String key = "hello_001";
        String value = "woca";

        boolean ans = cacheService.set(key, value, 600);
        logger.info("The result is: {}", ans);

        Object obj = cacheService.get(key);
        logger.info("The obj is {}", obj);


        Map<String, List<Point>> map = new HashMap<>(2);
        List<Point> pointList = new ArrayList<>();
        pointList.add(new Point("a1", 123f, 123));
        pointList.add(new Point("a2", 10f, 20f));
        map.put("a", pointList);

        List<Point> pointList2 = new ArrayList<>();
        pointList2.add(new Point("b1", 10f, 110f));
        pointList2.add(new Point("b2", -10f, -20f));
        pointList2.add(new Point("b3", -100f, -200f));
        map.put("b2", pointList2);

        String key2 = "world_001";
        boolean ans2 = cacheService.setObject(key2, map, 600);
        logger.info("The ans2 is  {}", ans2);
        Object result = cacheService.getObject(key2, map.getClass());
        logger.info("Thre result2 is {}", result);
    }


    @Test
    public void keySubTest() throws InterruptedException {
        for (int i = 0; i < 1; i++) {
            TestThread thread= new TestThread(cacheService);
            thread.start();
        }
        Thread.sleep(50000L);
    }


    @Test
    public void testCacheClient() throws Exception {
        int id = 1002;
        String name = "cache_test";

        // 注册回调函数
        cacheClient.register(name, new CacheCallBackInterface(){

            @Override
            public String getKey(int id) {
                return "test_" + id;
            }

            @Override
            public int getExpire() {
                return 30;
            }

            @Override
            public Object getObject(String key) {
                return key.length();
            }
        });

        Integer count = cacheClient.getObject(name, id ,Integer.class);
        logger.info("The count is : {}", count);


        cacheService.setObject("test_" + id, 1231234, 30);


        count = cacheClient.getObject(name, id, Integer.class);
        logger.info("The count is : {}", count);


        Thread.sleep(1000);
        count = cacheClient.getObject(name, id, Integer.class);
        logger.info("The count is : {}", count);
    }

}
