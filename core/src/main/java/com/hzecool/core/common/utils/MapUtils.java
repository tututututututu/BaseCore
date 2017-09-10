/*
 * 文件名称: MapUtils.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-5-8
 * 修改内容: 
 */
package com.hzecool.core.common.utils;


import android.text.TextUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * map工具类
 * 
 * @author zhangyz created on 2014-5-8
 */
public class MapUtils {
	

	
	/**
	 * 直接生成map，并传入key-value对
	 * 
	 * @param keysAndValues
	 * @return
	 * @author zhangyz created on 2014-5-8
	 */
	public static Map<String, Object> genMap(Object... keysAndValues) {
		Map<String, Object> ret = new HashMap<String, Object>();

		int len = keysAndValues.length;
		if (len == 0)
			return ret;

		checkLenth(keysAndValues);
		for (int i = 0; i < len; i += 2) {
			String key = String.valueOf(keysAndValues[i]);
			Object val = keysAndValues[i + 1];
			if (val != null)
				ret.put(key, val);
		}

		return ret;
	}

	/**
	 * 直接生成map，并传入key-value对
	 * 
	 * @param keysAndValues
	 *            key是整形，value是字符串
	 * @return
	 * @author zhangyz created on 2014-5-8
	 */
	public static Map<Integer, String> genIntStringMap(Object... keysAndValues) {
		Map<Integer, String> ret = new HashMap<Integer, String>();

		int len = keysAndValues.length;
		if (len == 0)
			return ret;

		checkLenth(keysAndValues);
		for (int i = 0; i < len; i += 2) {
			Integer key = (Integer) (keysAndValues[i]);
			String val = (String) keysAndValues[i + 1];
			if (val != null)
				ret.put(key, val);
		}

		return ret;
	}
	
	/**
	 * 直接生成Map ,并传入key-value对
	 * 
	 * @param keysAndValues
	 * 	key : String ,value : String
	 * @return
	 */
	public static Map<String,String> genStringStringMap(String...keysAndValues) {
		Map<String, String> ret = new HashMap<String, String>();
		
		int len = keysAndValues.length;
		if (len == 0)
			return ret;
		
		for (int i = 0; i < len; i += 2) {
			String key = keysAndValues[i];
			String val = keysAndValues[i + 1];
			if (val != null)
				ret.put(key, val);
		}
		return ret;
	}
	
	private static void checkLenth(Object...keysAndValues){
		if (keysAndValues.length % 2 != 0)
			throw new IllegalArgumentException("传入的参数必须成对!");
	}
	
	/**
	 * 直接生成map，并传入key-value对，若为空也加入
	 * 
	 * @param keysAndValues
	 * @return
	 * @author zhangyz created on 2014-5-8
	 */
	public static Map<String, Object> genMapWithNull(Object... keysAndValues) {
		Map<String, Object> ret = new HashMap<String, Object>();

		int len = keysAndValues.length;
		if (len == 0)
			return ret;

		if (len % 2 != 0)
			throw new IllegalArgumentException("传入的参数必须成对!");
		for (int i = 0; i < len; i += 2) {
			String key = String.valueOf(keysAndValues[i]);
			Object val = keysAndValues[i + 1];
			ret.put(key, val);
		}

		return ret;
	}

	/**
	 * 生成list列表
	 * 
	 * @param values
	 * @param <T>
	 * @return
	 */
	@SafeVarargs
	public static <T> List<T> genList(T... values) {
		List<T> ret = new java.util.ArrayList<>();
		if (values == null)
			return ret;

		for (T value : values) {
			ret.add(value);
		}
		return ret;
	}

	/**
	 * Map转换为String
	 * 
	 * @param map
	 * @return
	 * @Author:zhanggd created on 2015-7-15
	 */
	public static String mapToString(Map<String, ?> map) {
		if (map == null || map.size() == 0)
			return null;

		StringBuffer buffer = new StringBuffer();
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Object value = map.get(key);
			String strValue = "";
			if (value != null)
				strValue = value.toString();
			buffer.append(",");
			buffer.append(key).append(":").append(strValue);
		}
		return buffer.substring(1);
	}


	/**
	 * 将Map转为 key=val&key=val 形式
	 * 
	 * @param map
	 * @return
	 */
	public static String map2Url(Map<String, String> map) {
		if (map == null) {
			return null;
		}
		StringBuilder url = new StringBuilder();
		for (Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator(); iter.hasNext();) {
			Map.Entry<String, String> entry = iter.next();
			url.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		url.setLength(url.length() - 1);
		return url.toString();
	}

	
	public static String getStringValue(Map<String, ?> map ,String name,String... defaultValue){
        Object value = map.get(name);      
        String factValue = null;
        if (defaultValue != null && defaultValue.length > 0)
            factValue = defaultValue[0];
        
        if(value != null){
            return ((String)value).trim();
        }
        
        return factValue;
    }


	/**
	 * 移除map中的value空值
	 * @param map
	 * @return
	 */
	public static void removeNullValue(Map map){
		Set set = map.keySet();
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			Object obj = (Object) iterator.next();
			Object value =(Object)map.get(obj);
			remove(value, iterator);
		}
	}


	private static void remove(Object obj,Iterator iterator){
		if(obj instanceof String){
			String str = (String)obj;
			if(TextUtils.isEmpty(str)){
				iterator.remove();
			}
		}else if(obj instanceof Collection){
			Collection col = (Collection)obj;
			if(col==null||col.isEmpty()){
				iterator.remove();
			}

		}else if(obj instanceof Map){
			Map temp = (Map)obj;
			if(temp==null||temp.isEmpty()){
				iterator.remove();
			}

		}else if(obj instanceof Object[]){
			Object[] array =(Object[])obj;
			if(array==null||array.length<=0){
				iterator.remove();
			}
		}else{
			if(obj==null){
				iterator.remove();
			}
		}
	}

}
