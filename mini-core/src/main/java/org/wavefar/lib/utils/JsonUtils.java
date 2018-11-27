package org.wavefar.lib.utils;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.TreeMap;

/**
 * json 工具类
 * @author summer
 */
public class JsonUtils {

	private static final Gson gson = new Gson();

	/**
	 * 对象转json字符串
	 * @param src
	 * @return
	 */
	public static String toJson(Object src) {
		    if(src instanceof JSONObject) {
				return src.toString();
			}
			return gson.toJson(src);
	}

	/**
	 * json字符串转对象,解析多个实体类集合
	 * @param json 字符串
	 * @param typeOfT <pre>{@code new TypeToken<ArrayList<T>>(){}.getType();}</pre>
	 * @return T
	 */
	public static <T> T fromJson(String json, Type typeOfT) {
		try {
			return gson.fromJson(json, typeOfT);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * json字符串转对象,从json字符串解析为单个实体类
	 * @param json 字符串
	 * @param clazz 实体类
	 * @return T 实体类 
	 */
	public static <T> T fromJson(String json, Class<T> clazz) {
		try {
			return gson.fromJson(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将实体转化Hashmap
	 *
	 * @param bean 待转化的实体类
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static TreeMap<String, String> convertBeanToMap(Object bean)
			throws IllegalArgumentException, IllegalAccessException {
		Class<?> object = bean.getClass();
		TreeMap<String, String> data = new TreeMap<>();
		//父类字段
		Field[] superFields = object.getSuperclass().getDeclaredFields();
		for (Field field : superFields) {
			field.setAccessible(true);
			if (field.get(bean) != null) {
				data.put(field.getName(), String.valueOf(field.get(bean)));
			}
		}

		//子类字段
		Field[] fields = object.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.get(bean) != null) {
				data.put(field.getName(), String.valueOf(field.get(bean)));
			}
		}
		return data;
	}
}
