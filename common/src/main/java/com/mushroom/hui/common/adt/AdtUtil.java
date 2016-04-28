package com.mushroom.hui.common.adt;

import com.mushroom.hui.common.adt.entity.ResultCode;
import com.mushroom.hui.common.adt.entity.RetData;
import com.mushroom.hui.common.adt.entity.Status;

/**
 * Created by yihui on 16/4/1.
 */
public class AdtUtil {
    private AdtUtil() {
    }


    public static <T> RetData<T> returnResult(T result) {
        RetData<T> retData = new RetData<T>(result);
        return retData;
    }

    public static  RetData<?> returnErrorResult(ResultCode code, String... args) {
        Status status = new Status(code);
        status.mixMsg(args);
        RetData<?>retData = new RetData<Object>(status);
        return retData;
    }

}
