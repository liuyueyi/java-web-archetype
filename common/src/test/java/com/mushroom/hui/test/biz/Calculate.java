package com.mushroom.hui.test.biz;

import java.util.List;

/**
 * Created by yihui on 16/4/10.
 */
public class Calculate {

    public int add(int num) {
        return num << 2;
    }

    public String array(List<String> list, int size) {
        String str = list.toString();
        return str + " size: " + size;
    }
}
