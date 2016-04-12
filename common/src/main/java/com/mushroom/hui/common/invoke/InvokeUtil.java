package com.mushroom.hui.common.invoke;

import java.lang.reflect.Method;

/**
 * Created by yihui on 16/4/10.
 */
public class InvokeUtil {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(InvokeUtil.class);

    public static Object invoke(String strParams) {
        Params params = ParamUtil.buildParams(strParams);
        if (params == null || ! params.isValiad()) {
            return null;
        }

        try {
            Class clz = Class.forName(params.getCls());
            Object obj = clz.newInstance(); // 无参构造函数必须要有
            int size = params.getParams().size();
            if (size == 0) { // 没有参数
                Method method = clz.getMethod(params.getMethod());
                Object ans = method.invoke(obj);
                return ans;
            }

            Class<?>[] argTypes = new Class<?>[size];
            Object[] argValues = new Object[size];
            if (!ParamUtil.buildArgInfos(params, argTypes, argValues)) {
                return null;
            }

            Method method = clz.getMethod(params.getMethod(), argTypes);
            Object ans = method.invoke(obj, argValues);
            return ans;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
