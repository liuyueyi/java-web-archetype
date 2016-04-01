package com.mushroom.hui.common.adt.entity;

/**
 * Created by yihui on 16/1/18.
 */
public enum ResultCode {
    SUCCESS(1001, "SUCCESS"),
    FAIL(4004, "FAIL"),
    PARAMETER_ERROR(5001, "传入参数有误! %s : %s");

    int code;
    String msg;

    private ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
