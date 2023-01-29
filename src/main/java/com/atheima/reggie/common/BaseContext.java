package com.atheima.reggie.common;

/**
 * 基于ThreadLocal封装工具类，来获取当前用户的id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void set(Long id){
        threadLocal.set(id);
    }

    public static Long get(){
        return threadLocal.get();
    }
}
