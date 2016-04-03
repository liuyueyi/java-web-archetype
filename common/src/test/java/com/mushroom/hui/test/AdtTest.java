package com.mushroom.hui.test;

import com.mushroom.hui.common.adt.AdtUtil;
import com.mushroom.hui.common.adt.entity.ResultCode;
import com.mushroom.hui.common.adt.entity.RetData;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yihui on 16/4/1.
 */
public class AdtTest {

    @Test
    public void testAdtUtil() {
        List<String> ans1 = new ArrayList<>();
        ans1.add("hello");
        ans1.add("world");

        RetData<List<String>> ret = AdtUtil.returnResult(ans1);
        System.out.println(ret);

        ans1.add("hehe");
        System.out.println(ret);

        Map<String, String> map = new HashMap<>(5);
        map.put("1", "asdf");
        map.put("@", "呵呵");
        RetData<Map<String, String>> ret2 = AdtUtil.returnResult(map);
        System.out.println(ret2);


        RetData ret3 = AdtUtil.returnErrorResult(ResultCode.PARAMETER_ERROR, "testAdtUtil", "失败");
        System.out.println(ret3);


        RetData ret4 = AdtUtil.returnErrorResult(ResultCode.PARAMETER_ERROR);
        System.out.println(ret4);


        RetData ret5 = AdtUtil.returnErrorResult(ResultCode.PARAMETER_ERROR, "method2", "hehe");
        System.out.println(ret5);

        System.out.println(ret3);
        System.out.println(ret4);
    }

}
