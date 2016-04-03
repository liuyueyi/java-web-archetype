package com.mushroom.hui.test;

import com.mushroom.hui.common.cache.CacheWrapper;
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

    private CacheWrapper cacheWrapper;

    @Before
    public void init() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:spring/*.xml");
        cacheWrapper = applicationContext.getBean("cacheWrapper", CacheWrapper.class);
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

        boolean ans = cacheWrapper.set(key, value, 600);
        logger.info("The result is: {}", ans);

        Object obj = cacheWrapper.get(key);
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
        boolean ans2 = cacheWrapper.setObject(key2, map, 600);
        logger.info("The ans2 is  {}", ans2);
        Object result = cacheWrapper.getObject(key2, map.getClass());
        logger.info("Thre result2 is {}", result);
    }


}
