package com.win.utils;

import java.util.Map;

/**
 * ThreadLocal 工具类
 */
public class ThreadLocalUtil {
    //提供ThreadLocal对象
    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<>();//声明了threadlocal对象，对象是键值对

    //根据键获取值
    @SuppressWarnings("unchecked") //忽略警告,在这里因为从ThreadLocal中获取的值在编译器看来可能存在类型转换不明确的情况
    public static Map<String, Object> get(){
        return THREAD_LOCAL.get();
    }
	
    //存储键值对
    public static void set(Map<String, Object> map){
        THREAD_LOCAL.set(map);
    }

    //清除ThreadLocal 防止内存泄漏
    public static void remove(){
        THREAD_LOCAL.remove();
    }
}
