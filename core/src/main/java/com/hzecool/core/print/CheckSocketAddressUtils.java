package com.hzecool.core.print;

import com.hzecool.core.common.utils.RegexUtils;
import com.hzecool.core.log.L;

/**
 * Created by 47066 on 2017/9/11.
 */

public class CheckSocketAddressUtils {
    public static boolean checkAddress(int port, String ip) {

        if (port > 0 && port < 65535) {
            if (RegexUtils.isIP(ip)) {
                L.logFile("打印端口正则检测有效");
                return true;
            }
        }
        L.logFile("打印端口正则检测无效");
        return false;
    }
}
