package com.newcoder.communitydemo.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 对Cookie的工具类
 */
public class CookieUtil {
    /**
     * 用来获取HttpServletRequest的cookie
     * @param request
     * @param name
     * @return
     */
    public static String getValue(HttpServletRequest request, String name) {
        if (request == null || name == null) {
            throw new IllegalArgumentException("参数为空");
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
