package com.hzecool.core.common.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import java.util.Locale;

/**
 *
 * Created by song on 2017/7/11.
 */

public class LanguageUtils {

    /**
     * 获取系统语言的种类
     * @return
     */
    public static String getLanguageType(){
        String languageType = "";
        Locale locale = Locale.getDefault();
        //获取的语种,比如 英文,汉语,法语
        String language = locale.getLanguage();
        //获取的是语言的种类,比如若设置的是中文,返回的就是简体cn或者繁体tw
        String country = locale.getCountry();
        if(language.equals("zh")){
            if(country.equals("CN")){
                languageType = languageType + "cn";
            }else if(country.equals("TW") || country.equals("HK")){
                languageType = languageType + "zh-tw";
            }
        }else if(language.equals("en")){
            languageType = languageType + "en";
        }else {
            languageType = languageType + "cn";
        }
        return languageType;
    }


    /**
     * 获取系统语言的种类
     * 0 -->中文繁体
     * 1 -->中文简体
     */

    public static String getType(){
        String type = "";
        Locale locale = Locale.getDefault();
        //获取的语种,比如 英文,汉语,法语
        String language = locale.getLanguage();
        //获取的是语言的种类,比如若设置的是中文,返回的就是简体cn或者繁体tw
        String country = locale.getCountry();
        if(language.equals("zh")){
            if(country.equals("CN")){
                type = type + "1";
            }else if(country.equals("TW") || country.equals("HK")){
                type = type + "0";
            }
        }else if(language.equals("en")){
            type = type + "1";
        }else {
            type = type + "1";
        }
        return type;
    }


    private static final String LAST_LANGUAGE = "lastLanguage";

    /**
     * 当改变系统语言时,重启App
     *
     * @param activity
     * @param homeActivityCls 主activity
     * @return
     */
    public static boolean isLanguageChanged(Activity activity, Class<?> homeActivityCls) {
        Locale locale = Locale.getDefault();
        if (locale == null) {
            return false;
        }
       /* SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        String localeStr = sp.getString(LAST_LANGUAGE, "");*/
        String localeStr = SPUtils.getString(LAST_LANGUAGE, "");
        String curLocaleStr = getLocaleString(locale);
        if (TextUtils.isEmpty(localeStr)) {
          //  sp.edit().putString(LAST_LANGUAGE, curLocaleStr).commit();
            SPUtils.putString(LAST_LANGUAGE, curLocaleStr);
            return false;
        } else {
            if (localeStr.equals(curLocaleStr)) {
                return false;
            } else {
               // sp.edit().putString(LAST_LANGUAGE, curLocaleStr).commit();
                SPUtils.putString(LAST_LANGUAGE, curLocaleStr);
                restartApp(activity, homeActivityCls);
                return true;
            }
        }
    }

    private static String getLocaleString(Locale locale) {
        if (locale == null) {
            return "";
        } else {
            return locale.getCountry() + locale.getLanguage();
        }
    }

    public static void restartApp(Activity activity, Class<?> homeClass) {
        Intent intent = new Intent(activity, homeClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        // 杀掉进程
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
