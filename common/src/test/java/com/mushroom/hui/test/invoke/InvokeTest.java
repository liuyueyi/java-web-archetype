package com.mushroom.hui.test.invoke;

import com.alibaba.fastjson.JSON;
import com.mushroom.hui.common.invoke.InvokeUtil;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yihui on 16/4/10.
 */
public class InvokeTest {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(InvokeTest.class);

    @Test
    public void invokeTest() {
        String clz = "com.mushroom.hui.test.biz.Calculate";
        String mtd = "add";
        try {
            Class cls = Class.forName(clz);
            Method[] methods = cls.getDeclaredMethods();
            Method method = cls.getMethod(mtd, int.class);
            Object ans = method.invoke(cls.newInstance(), 20);
            logger.info("The ans is : {}", ans);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("e: {}", e);
        }
    }

    private String getAddArg() {
        Map<String, String> map = new HashMap<>(3);
        map.put("target", "invoke");
        map.put("class", "com.mushroom.hui.test.biz.Calculate");
        map.put("method", "add");

        Map<String, String> args = new HashMap<>();
        args.put("java.lang.Integer", "10");

        map.put("params", JSON.toJSONString(args));
        String strParams = JSON.toJSONString(map);
        System.out.println(strParams);
        return strParams;
    }

    private String getArrayArg() {
        Map<String, String> map = new HashMap<>(3);
        map.put("target", "invoke");
        map.put("class", "com.mushroom.hui.test.biz.Calculate");
        map.put("method", "array");
        List<String> list = new ArrayList<>();
        list.add("123");
        list.add("hello");
        list.add("world");
        Map<String, String> args = new HashMap<>();
        args.put("java.util.List", JSON.toJSONString(list));
        args.put("int", "100");
        map.put("params", JSON.toJSONString(args));

        String strParams =  JSON.toJSONString(map);
        System.out.println(strParams);
        return strParams;
    }

    @Test
    public void paramsTest() {
        // invoke
        Object obj = InvokeUtil.invoke(getAddArg());
        System.out.println(obj);

        obj = InvokeUtil.invoke(getArrayArg());
        System.out.println(obj);
    }

    @Test
    public void jsonTest() {
        String str = "10";
        Object obj = JSON.parseObject(str, int.class);
        System.out.println(obj);
    }
}
