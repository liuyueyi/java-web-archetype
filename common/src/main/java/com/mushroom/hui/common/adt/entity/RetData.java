package com.mushroom.hui.common.adt.entity;

import java.io.Serializable;

/**
 * Created by yihui on 16/1/17.
 */
public class RetData<T> implements Serializable {

    private static final long serialVersionUID = -7658519418208586385L;

    private Status status;
    private T result;
    private boolean isSuccess;

    public RetData() {
        status = Status.getSuccessStatus();
        isSuccess = true;
    }

    public RetData(T result) {
        this();
        this.result = result;
    }

    public RetData(Status status) {
        this.status = status;
        this.isSuccess = false;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        isSuccess = isSuccess;
    }

    @Override
    public String toString() {
        return "RetData{" +
                "status=" + status +
                ", result=" + result +
                '}';
    }
}
