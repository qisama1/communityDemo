package com.newcoder.communitydemo.config;

import com.newcoder.communitydemo.controller.interceptor.AlphaInterceptor;
import com.newcoder.communitydemo.controller.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器的配置类
 */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private AlphaInterceptor alphaInterceptor;
    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    /**
     * 在SpringMVC中添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(alphaInterceptor); // 这会拦截一切请求
        registry.addInterceptor(alphaInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/**.png", "/**/*.jpeg")
                .addPathPatterns("/register", "/login"); // 不拦截静态资源 + 具体拦截资源

        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/**.png", "/**/*.jpeg");
    }
}
