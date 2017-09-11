package com.hzecool.core.update.provider;


/**
 * 升级返回实体类
 * Created by tutu on 2017/5/25.
 */

public class NetUpdateBean {

    /**
     * upgradeFlag : 1 有升级 ，0无升级
     * upgradeMsg : {"upgradeMessage":"升级提示信息","noticemethod":"升级类型(0:通知、1:提示升级、2:强制升级)","upgrademethod":"ios升级方式(0:appstore, 1:testflight)","androidupgradeurl":"android升级url"}
     */

    private String upgradeFlag;
    private UpgradeMsgBean upgradeMsg;

    public String getUpgradeFlag() {
        return upgradeFlag;
    }

    public void setUpgradeFlag(String upgradeFlag) {
        this.upgradeFlag = upgradeFlag;
    }

    public UpgradeMsgBean getUpgradeMsg() {
        return upgradeMsg;
    }

    public void setUpgradeMsg(UpgradeMsgBean upgradeMsg) {
        this.upgradeMsg = upgradeMsg;
    }

    public static class UpgradeMsgBean {
        /**
         * upgradeMessage : 升级提示信息
         * noticemethod : 升级类型(0:通知、1:提示升级、2:强制升级)
         * upgrademethod : ios升级方式(0:appstore, 1:testflight)
         * androidupgradeurl : android升级url
         */

        private String upgradeMessage;
        private String noticemethod;
        private String upgrademethod;
        private String androidupgradeurl;

        public String getUpgradeMessage() {
            return upgradeMessage;
        }

        public void setUpgradeMessage(String upgradeMessage) {
            this.upgradeMessage = upgradeMessage;
        }

        public String getNoticemethod() {
            return noticemethod;
        }

        public void setNoticemethod(String noticemethod) {
            this.noticemethod = noticemethod;
        }

        public String getUpgrademethod() {
            return upgrademethod;
        }

        public void setUpgrademethod(String upgrademethod) {
            this.upgrademethod = upgrademethod;
        }

        public String getAndroidupgradeurl() {
            return androidupgradeurl;
        }

        public void setAndroidupgradeurl(String androidupgradeurl) {
            this.androidupgradeurl = androidupgradeurl;
        }

        @Override
        public String toString() {
            return "UpgradeMsgBean{" +
                    "upgradeMessage='" + upgradeMessage + '\'' +
                    ", noticemethod='" + noticemethod + '\'' +
                    ", upgrademethod='" + upgrademethod + '\'' +
                    ", androidupgradeurl='" + androidupgradeurl + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NetUpdateBean{" +
                "upgradeFlag='" + upgradeFlag + '\'' +
                ", upgradeMsg=" + upgradeMsg +
                '}';
    }
}
