package com.hzecool.core.common.utils;

import java.math.BigDecimal;

/**
 * 数学计算工具类
 * Created by tutu on 2017/5/15.
 */

public class MathUtils {
    /**
     * 根据 type和 count保留对应的小数位和取整模式
     *
     * @param bigDecimal
     * @param count      保留的小数位数
     * @param type       保留的模式 1.向上取整  2.向下取整 3.四舍五入取整
     * @return
     */
    public static BigDecimal keepDecimalPlaces(BigDecimal bigDecimal, int count, int type) {
        if (bigDecimal == null) {
            return null;
        }

        switch (type) {
            /**
             * 向上取整
             */
            case BigDecimal.ROUND_UP:
                return bigDecimal.setScale(count, BigDecimal.ROUND_UP);
            /**
             * 向下取整
             */
            case BigDecimal.ROUND_DOWN:
                return bigDecimal.setScale(count, BigDecimal.ROUND_DOWN);
            /**
             * 四舍五入
             */
            case BigDecimal.ROUND_HALF_UP:
                return bigDecimal.setScale(count, BigDecimal.ROUND_HALF_UP);

        }
        return bigDecimal.setScale(count, BigDecimal.ROUND_HALF_UP);
    }


    public static String convertShowNum(Float count) {
        if (Math.abs(count - (int) count.floatValue()) > 0) {
            return count + "";
        } else {
            return (int) count.floatValue() + "";
        }
    }

    /**
     * 返回有效位数
     *
     * @param count
     * @return
     */
    public static String convertShowNum(BigDecimal count) {
        if (Math.abs(count.floatValue()) < 0.001) {
            return "0";
        }
        return count.stripTrailingZeros().toPlainString();
    }
}
