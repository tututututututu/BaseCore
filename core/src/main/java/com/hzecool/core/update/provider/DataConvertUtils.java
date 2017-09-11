package com.hzecool.core.update.provider;

import com.hzecool.core.log.L;

/**
 * Created by 47066 on 2017/9/11.
 */

public class DataConvertUtils {
    public static UpdateBean updateBeanConvert(NetUpdateBean netUpdateBean) {

        if (netUpdateBean == null) {
            L.logFile("DataConvertUtils->updateBeanConvert()" + "netUpdateBean = null");
            return null;
        }
        UpdateBean updateBean = new UpdateBean();
        updateBean.setUpdate("0".equals(netUpdateBean.getUpgradeFlag()) ? false : true);
        updateBean.setTips(netUpdateBean.getUpgradeMsg().getUpgradeMessage());
        updateBean.setForceUpdate("2".equals(netUpdateBean.getUpgradeMsg().getNoticemethod()) ? true : false);
        updateBean.setUrl(netUpdateBean.getUpgradeMsg().getAndroidupgradeurl());
        return updateBean;
    }
}
