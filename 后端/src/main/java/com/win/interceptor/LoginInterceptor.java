package com.win.interceptor;

import com.win.utils.JwtUtil;
import com.win.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

/**
 * 登录拦截器，用于拦截请求以验证用户登录状态
 * 通过检查请求头中的Authorization信息，来确定用户是否已经登录
 * 主要功能包括：
 * 1. 验证用户登录状态
 * 2. 将用户信息保存到ThreadLocal中，以便后续使用
 * 3. 请求完成后清除ThreadLocal中的信息
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    //HandlerInterceptor提供了preHandle（请求处理前）、postHandle（请求处理后）、afterCompletion（请求完成后）的接口，在这里实现了这些拦截接口
    /**
     * 在请求处理之前进行拦截
     * 
     * @param request  请求对象，用于获取请求头中的Authorization信息
     * @param response 响应对象，用于设置响应状态码
     * @param handler  处理请求的对象，用于后续处理
     * @return boolean 返回值决定是否继续执行后续请求处理
     * @throws Exception 抛出异常表示拦截器处理失败
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的Authorization信息
        String token = request.getHeader("Authorization");
        try {
            // 解析token，获取用户信息
            Map<String, Object> claims = JwtUtil.parseToken(token);
            // 将用户信息保存到ThreadLocal中，以便后续使用
            ThreadLocalUtil.set(claims);
            // 验证通过，继续执行后续请求处理
            return true;
        } catch (Exception e) {
            // 验证失败，设置响应状态码为401，表示未授权
            response.setStatus(401);
            // 中断后续请求处理
            return false;
        }
    }

    /**
     * 请求处理完成后进行拦截
     * 
     * @param request  请求对象
     * @param response 响应对象
     * @param handler  处理请求的对象
     * @param ex       请求处理过程中出现的异常
     * @throws Exception 抛出异常表示拦截器处理失败
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求处理完成后，清除ThreadLocal中的信息
        ThreadLocalUtil.remove();
    }
}