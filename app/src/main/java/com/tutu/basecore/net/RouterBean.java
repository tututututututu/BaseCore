package com.tutu.basecore.net;

/**
 * Created by tutu on 2017/4/14.
 */

public class RouterBean {
    /**
     * epid : 3
     * psw : yYSu0BSux2I6VPBZHaB6hf1Ldi0=
     * urlSpare : http://115.231.110.248:8048
     * code : 0411
     * urlMain : http://115.231.110.248:8048
     */

    private String epid;
    private String psw;
    private String urlSpare;
    private String code;
    private String urlMain;

    public String getEpid() {
        return epid;
    }

    public void setEpid(String epid) {
        this.epid = epid;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getUrlSpare() {
        return urlSpare;
    }

    public void setUrlSpare(String urlSpare) {
        this.urlSpare = urlSpare;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrlMain() {
        return urlMain;
    }

    public void setUrlMain(String urlMain) {
        this.urlMain = urlMain;
    }

    @Override
    public String toString() {
        return "RouterBean{" +
                "epid='" + epid + '\'' +
                ", psw='" + psw + '\'' +
                ", urlSpare='" + urlSpare + '\'' +
                ", code='" + code + '\'' +
                ", urlMain='" + urlMain + '\'' +
                '}';
    }
}
