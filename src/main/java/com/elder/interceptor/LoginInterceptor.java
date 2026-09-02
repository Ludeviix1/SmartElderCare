package com.elder.interceptor;

import com.elder.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
            return false;
        }

        try {
            //token非法、或者过期，解析都会抛出异常
            Map<String, Object> map = JwtUtil.parseToken(token);
            //token合法，放行
            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
