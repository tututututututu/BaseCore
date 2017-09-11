package com.hzecool.core.update.provider;

/**
 * 升级用的实体类
 * Created by 47066 on 2017/9/11.
 */

public class UpdateBean {
    private String url;
    private boolean isUpdate;
    private boolean forceUpdate;
    private String tips;
    private String serverVersion;

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    @Override
    public String toString() {
        return "UpdateBean{" +
                "url='" + url + '\'' +
                ", isUpdate=" + isUpdate +
                ", forceUpdate=" + forceUpdate +
                ", tips='" + tips + '\'' +
                ", serverVersion='" + serverVersion + '\'' +
                '}';
    }
}
