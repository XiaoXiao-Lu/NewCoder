package com.kinnon.config;

import com.kinnon.controller.intercepter.AlphaInterceter;
import com.kinnon.controller.intercepter.LoginRequirdInterceter;
import com.kinnon.controller.intercepter.LoginTicketInterceter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Kinnon
 * @create 2022-08-06 11:50
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AlphaInterceter alphaInterceter;

    @Autowired
    private LoginTicketInterceter loginTicketInterceter;

    @Autowired
    private LoginRequirdInterceter loginRequirdInterceter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alphaInterceter)
        .excludePathPatterns("/*/*.css","/*/*.js","/*/*.png","/*/*.jpeg")
                .addPathPatterns("/register","/login");


        registry.addInterceptor(loginTicketInterceter)
                .excludePathPatterns("/*/*.css","/*/*.js","/*/*.png","/*/*.jpeg");

        registry.addInterceptor(loginRequirdInterceter)
                .excludePathPatterns("/*/*.css","/*/*.js","/*/*.png","/*/*.jpeg");



    }
}
