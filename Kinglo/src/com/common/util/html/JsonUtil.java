package com.common.util.html;

import com.common.result.ResponseResult;
import com.common.util.bean.BeanUtil;

import java.util.Date;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public final class JsonUtil {
    private static JsonConfig cfg = new JsonConfig();

    static {
	cfg.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor());
    }
    
    /**
     * Java Bean转换为JSON对象
     * @param bean Java Bean对象
     * @return JSONObject
     */
    public static JSONObject bean2JsonObject(Object bean) {
	if (bean != null) {
	    BeanUtil.setUninitializeProperties2null(bean);
	}
	return JSONObject.fromObject(bean, cfg);
    }
    
    /**
     * Java Bean转换为JSON对象
     * @param bean Java Bean对象
     * @return 字符串
     */
    public static String bean2JsonObjectString(Object bean) {
	if (bean != null) {
	    BeanUtil.setUninitializeProperties2null(bean);
	}
	return JSONObject.fromObject(bean, cfg).toString();
    }
    
    /**
     * Java Bean转换为JsonArray
     * @param bean Java Bean对象
     * @return JsonArray
     */
    public static JSONArray bean2JsonArray(Object bean) {
	if (bean != null) {
	    BeanUtil.setUninitializeProperties2null(bean);
	}
	return JSONArray.fromObject(bean, cfg);
    }
    
    /**
     * Java Bean转换为JsonArray
     * @param bean Java Bean对象
     * @return 字符串
     */
    public static String bean2JsonArrayString(Object bean) {
	if (bean != null) {
	    BeanUtil.setUninitializeProperties2null(bean);
	}
	return JSONArray.fromObject(bean, cfg).toString();
    }
    
    /**
     * Json转换为Java Bean
     * @param <T>
     * @param jsonObjectString JSON字符串
     * @param beanClass        Class
     * @return
     */
    public static <T> T json2Bean(String jsonObjectString, Class<T> beanClass) {
	JSONObject jsonObject = JSONObject.fromObject(jsonObjectString);

	T bean = (T)JSONObject.toBean(jsonObject, beanClass);

	return bean;
    }
    
    /**
     * Json转换为Java Bean
     * @param <T>
     * @param jsonArrayString jsonArray字符串
     * @param beanClass	      Class
     * @return
     */
    public static <T> T[] json2BeanArray(String jsonArrayString,
	    Class<T> beanClass) {
	JSONArray jsonObject = JSONArray.fromObject(jsonArrayString);
	Object[] beans = (Object[]) JSONArray.toArray(jsonObject, beanClass);
	return (T[])beans;
    }
    
    /**
     * Json转换为List列表的Java Bean
     * @param <T>
     * @param jsonArrayString
     * @param beanClass
     * @return
     */
    public static <T> List<T> json2BeanList(String jsonArrayString,
	    Class<T> beanClass) {
	JSONArray jsonObject = JSONArray.fromObject(jsonArrayString);
	List<T> beans = JSONArray.toList(jsonObject, beanClass);
	return beans;
    }
    
    /**
     * ExjsFormReponse对象转换为JSON
     * @param response ExtjsFormResponse对象
     * @return
     */
    public static String getExjsFormReponseJson(ResponseResult response) {
	return getExjsFormReponseJson(response.isSuccess(), response.getMsg());
    }
    
    /**
     * ExjsFormReponse对象转换为JSON
     * @param success 是否成功
     * @return
     */
    public static String getExjsFormReponseJson(boolean success) {
	return getExjsFormReponseJson(success, null);
    }
    
    /**
     * ExjsFormReponse对象转换为JSON
     * @param success 是否成功
     * @param msg     消息
     * @return
     */
    public static String getExjsFormReponseJson(boolean success, String msg) {
	StringBuffer sb = new StringBuffer();
	sb.append("{success:").append(success);
	if (msg != null) {
	    sb.append(",msg:\"").append(msg).append("\"");
	}
	sb.append("}");
	return sb.toString();
    }
}
