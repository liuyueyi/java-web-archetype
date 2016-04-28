package com.mushroom.hui.common.register.exception;

/**
 * Created by yihui on 16/4/5.
 */
public class RegisterException extends Exception {

    public RegisterException(String msg) {
        super(msg);
    }


    public RegisterException(String msg, Exception e) {
        super(msg, e);
    }

}
