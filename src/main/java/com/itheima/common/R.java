package com.itheima.common;

import java.util.HashMap;
import java.util.Map;


/**
 * 通用返回结果类，服务端响应的数据最终都会封装为此对象
 *
 * @param <T>
 */
public class R<T> {
    private Integer code;//编码：1 成功 0 失败

    private String msg;//错误信息

    private T data;//返回的数据

    private Map map = new HashMap();//动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.code = 1;
        r.data = object;
        return r;

    }


    public static <T> R<T> error(String msg) {
        R<T> r = new R<T>();
        r.code = 0;
        r.msg = msg;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "R{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", map=" + map +
                '}';
    }
}
