package com.newcoder.communitydemo.controller.interceptor;

import com.newcoder.communitydemo.annotation.LoginRequired;
import com.newcoder.communitydemo.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    /**
     * 在调用方法前，取判断它是否有LoginRequired注解或者user是否为空
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            if (method.getAnnotation(LoginRequired.class) != null && hostHolder.getUser() == null ) {
                response.sendRedirect(request.getContextPath() + "/login");
                return false; // 尝试去取注解
            }
        }
        return true;
    }
}
