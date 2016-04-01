package com.mushroom.hui.common.adt.entity;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * Created by yihui on 16/1/17.
 */
public class Status implements Serializable{
    private static final long serialVersionUID = -1470575617124127068L;

    private static Status SUCCESS_STATUS = new Status(ResultCode.SUCCESS);
    private static Status FAIL_STATUS = new Status(ResultCode.FAIL);

    private int code;
    private String msg;

    public Status() {
    }

    public Status(ResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getMsg());
    }

    public Status(int code, String msg) {
        this.msg = msg;
        this.code = code;
    }


    public static Status getSuccessStatus() {
        return SUCCESS_STATUS;
    }

    public static Status getFailStatus() {
        return FAIL_STATUS;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void mixMsg(String ...args) {
        if (StringUtils.isBlank(msg) || args.length == 0) {
            return;
        }

        msg = String.format(msg, args);
    }

    @Override
    public String toString() {
        return "Status{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
