package com.hzecool.core.net;

/**
 * 业务异常定义 处理
 * Created by tutu on 2017/1/10.
 */

public class BussinessException extends Exception {
    private String code;

    public BussinessException() {
        this(null, null);
    }

    public BussinessException(String code, String msg) {
        super(msg);
        this.code = code;
        switch (code) {
            case "404":
                break;
            //登录失效
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
