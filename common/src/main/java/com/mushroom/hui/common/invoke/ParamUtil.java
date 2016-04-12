package com.mushroom.hui.common.invoke;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yihui on 16/4/10.
 */
public class ParamUtil {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ParamUtil.class);

    /**
     * 将传入的参数,转换为Params对象
     * @param strPamras  {"target":"invoke","class":"com.mushroom.hui.test.biz.Calculate","method":"add","parameter":"10"}
     * @return
     */
    public static Params buildParams(String strPamras) {
        if (StringUtils.isBlank(strPamras)) {
            return null;
        }


        try {
            Map<String, String> map = JSON.parseObject(strPamras, Map.class);
            if (CollectionUtils.isEmpty(map)) {
                return null;
            }

            Params params = new Params();
            params.setTarget(map.get("target"));
            params.setCls(map.get("class"));
            params.setMethod(map.get("method"));

            String args = map.get("params");
            if (StringUtils.isBlank(args)) {
                return params;
            }

            Map<String, String> argMap = JSON.parseObject(args, Map.class);
            params.setParams(argMap);
            return params;

        }catch (Exception e) {
            logger.error("parse params to object error!");
            logger.error("Exception: {}", e);
            return  null;
        }
    }


    /**
     * 获取反射的方法对应参数信息
     * @param params 传入的反射信息
     * @param argTypes 参数类型
     * @param argValues 参数value
     * @return true 表示解析成功; false 表示解析失败
     */
    public static boolean buildArgInfos(Params params, Class<?>[] argTypes, Object[] argValues) {
        Map<String, String> argus = params.getParams();
        try {
            Class<?> clz;
            String value;
            int index = 0;
            for (Map.Entry<String, String> arg : argus.entrySet()) {
                clz = getBaseClass(arg.getKey());
                argTypes[index] = clz;
                value = arg.getValue();
                argValues[index++] = JSON.parseObject(value, clz);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private static Map<String, Class<?>> baseClass;
    static {
        baseClass = new HashMap<>(8);
        baseClass.put("short", short.class);
        baseClass.put("int", int.class);
        baseClass.put("long", long.class);
        baseClass.put("byte", byte.class);
        baseClass.put("float", float.class);
        baseClass.put("double", double.class);
        baseClass.put("boolean", boolean.class);
        baseClass.put("char", char.class);
    }

    private static Class<?> getBaseClass(String arg) throws ClassNotFoundException {
        if (StringUtils.isBlank(arg)) {
            return null;
        }

        if (baseClass.containsKey(arg)) {
            return baseClass.get(arg);
        } else {
            return Class.forName(arg);
        }
    }
}
