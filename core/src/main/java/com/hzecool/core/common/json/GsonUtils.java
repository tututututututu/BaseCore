package com.hzecool.core.common.json;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Gson转换工具类
 * Created by tutu on 17/2/15.
 */

public class GsonUtils {
    /**
     * 将Map转化为Json字符串
     *
     * @param map 要转的map
     * @return String
     */
    public static <T> String mapToJsonStr(Map<String, T> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }

    /**
     * 将Json字符串转化为Map
     * jsonStr Json字符串
     *
     * @return String
     */
    public static <T> Map<String, T> jsonStrToMap(String jsonStr) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, T>>() {
        }.getType();
        return gson.fromJson(jsonStr, type);
    }

    /**
     * map转为对象
     *
     * @param classOfT
     * @param <T>
     * @return
     */
    public static <T> T jsonToObj(String jsonStr, Class<T> classOfT) {
        return new Gson().fromJson(jsonStr, classOfT);
    }

    /**
     * json转ArryList
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(new Gson().fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

    /**
     * List转json字符串
     *
     * @param list
     * @return
     */
    public static <T> String listToJson(List<T> list) {

        Gson gson = new Gson();
        String jsonstring = gson.toJson(list);
        return jsonstring;

    }

    /**
     * 对象转json字符串
     */
    public static <T> String object2Json(T t) {
        Gson gson = new Gson();
        return gson.toJson(t);
    }

}
