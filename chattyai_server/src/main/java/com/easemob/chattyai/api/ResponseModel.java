package com.easemob.chattyai.api;


import com.easemob.chattyai.enums.ResponseModelEnum;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.api
 * @Author: alonecoder
 * @CreateTime: 2024-03-28  22:19
 * @Description: 响应实体
 * @Version: 1.0
 */
public class ResponseModel extends HashMap<String, Object> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * @description:自定义成功错误
     * @author: alonecoder
     * @date: 2024-03-28  22:19
     **/
    public ResponseModel() {
        // 响应码
        put("code", ResponseModelEnum.OK.getCode());
        // 响应信息
        put("message", ResponseModelEnum.OK.getMessage());
    }

    /**
     * @description:未知异常的错误返回
     * @author: alonecoder
     * @date: 2024-03-28  22:19
     * @return: ResponseModel
     **/
    public static ResponseModel error() {
        return error(ResponseModelEnum.ERROR.getCode(), ResponseModelEnum.ERROR.getMessage());
    }

    /**
     * @description:自定义错误信息的错误返回
     * @author: alonecoder
     * @date: 2024-03-28  22:19
     * @return: ResponseModel
     **/
    public static ResponseModel error(String message) {
        return error(ResponseModelEnum.ERROR.getCode(), message);
    }

    /**
     * @description:自定义错误码和错误信息的错误返回
     * @author: alonecoder
     * @date: 2024-03-28  22:19
     * @return: ResponseModel
     **/
    public static ResponseModel error(String code, String message) {
        ResponseModel r = new ResponseModel();
        r.put("code", code);
        r.put("message", message);
        return r;
    }

    /**
     * @description:成功返回
     * @author: alonecoder
     * @date: 2024-03-28  22:19
     * @return: ResponseModel
     **/
    public static ResponseModel ok(String message) {
        ResponseModel r = new ResponseModel();
        r.put("message", message);
        return r;
    }

    /**
     * @description:自定义成功code和信息的成功返回
     * @author: alonecoder
     * @date: 2024-03-28  22:19
     * @return: ResponseModel
     **/
    public static ResponseModel ok(String code, String message) {
        ResponseModel r = new ResponseModel();
        r.put("message", message);
        r.put("code", code);
        return r;
    }

    /**
     * @description:自定义成功内容的成功返回
     * @author: alonecoder
     * @date: 2024-03-28  22:19
     * @return: ResponseModel
     **/
    public static ResponseModel ok(Map<String, Object> map) {
        ResponseModel r = new ResponseModel();
        r.putAll(map);
        return r;
    }

    /**
     * @description:自定义成功内容的成功返回
     * @author: alonecoder
     * @date: 2024-03-28  22:19
     * @return: ResponseModel
     **/
    public static ResponseModel ok(Object o) {
        ResponseModel r = new ResponseModel();
        r.put("data", o);
        return r;
    }

    /**
     * @description:成功返回
     * @author: alonecoder
     * @date: 2024-03-28  22:19
     * @return: ResponseModel
     **/
    public static ResponseModel ok() {
        return new ResponseModel();
    }

    @Override
    public ResponseModel put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}

