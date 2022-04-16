package com.newcoder.communitydemo.controller.interceptor;

import com.newcoder.communitydemo.entity.LoginTicket;
import com.newcoder.communitydemo.entity.User;
import com.newcoder.communitydemo.service.UserService;
import com.newcoder.communitydemo.util.CookieUtil;
import com.newcoder.communitydemo.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * LoginTicket的拦截器
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor{
    @Autowired
    private UserService userService;

    @Autowired
    HostHolder hostHolder;

    /**
     * 在controller调用之前，先从浏览器的cookie中获取凭证信息
     * 在把cookie信息添加到hostHolder当中去
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从cookie种获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");
        if (ticket != null) {
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                User user = userService.findUserById(loginTicket.getUserId());
                // 在本次请求中持有用户
                hostHolder.setUsers(user);
            }
        }
        return true;
    }

    /**
     * 在调用模板之前把User信息添加到modelandview当中去
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    /**
     * 在一次请求中后把hosthold给清空
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
