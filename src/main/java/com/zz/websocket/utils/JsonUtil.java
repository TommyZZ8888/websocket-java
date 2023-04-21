package com.zz.websocket.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Description JsonUtil
 * @Author 张卫刚
 * @Date Created on 2023/4/21
 */
public class JsonUtil {


    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    public static String parseObjToJson(Object object) {
        String result = null;

        try {
            result = JSONObject.toJSONString(object);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return result;
    }

    public static <T> T parseJsonToObj(String json, Class<T> tClass) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(json);
            return jsonObject.toJavaObject(tClass);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }
}
