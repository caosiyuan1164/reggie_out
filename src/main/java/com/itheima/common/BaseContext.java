package com.itheima.common;

/**
 * 基于ThreadLocal封装工具类
 */
public class BaseContext {
    private static ThreadLocal<Long> local = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        local.set(id);
    }

    public static Long getCurrentId(){
        return local.get();
    }
}
