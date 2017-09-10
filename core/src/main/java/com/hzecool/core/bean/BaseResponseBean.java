package com.hzecool.core.bean;

import java.io.Serializable;

/**
 * 所有服务器返回数据结构的基类
 * Created by tutu on 2017/1/10.
 */

public class BaseResponseBean<T> implements Serializable {
    String msg;
    String code;
    String error;
    String result;
    String reason;
    T dataList;
    T data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public T getDataList() {
        return dataList;
    }

    public void setDataList(T dataList) {
        this.dataList = dataList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResponseBean{" +
                "msg='" + msg + '\'' +
                ", code='" + code + '\'' +
                ", error='" + error + '\'' +
                ", result='" + result + '\'' +
                ", reason='" + reason + '\'' +
                ", dataList=" + dataList +
                ", data=" + data +
                '}';
    }
}
