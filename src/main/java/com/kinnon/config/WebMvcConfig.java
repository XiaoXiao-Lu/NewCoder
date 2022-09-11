package com.kinnon.config;

import com.kinnon.controller.intercepter.*;
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

//    @Autowired
//    private LoginRequirdInterceter loginRequirdInterceter;

    @Autowired
    private MessageInterceter messageInterceter;

    @Autowired
    private DataInterceptor dataInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alphaInterceter)
        .excludePathPatterns("/*/*.css","/*/*.js","/*/*.png","/*/*.jpeg")
                .addPathPatterns("/register","/login");


        registry.addInterceptor(loginTicketInterceter)
                .excludePathPatterns("/*/*.css","/*/*.js","/*/*.png","/*/*.jpeg");

//        registry.addInterceptor(loginRequirdInterceter)
//                .excludePathPatterns("/*/*.css","/*/*.js","/*/*.png","/*/*.jpeg");

        registry.addInterceptor(messageInterceter)
                .excludePathPatterns("/*/*.css","/*/*.js","/*/*.png","/*/*.jpeg");

        registry.addInterceptor(dataInterceptor)
                .excludePathPatterns("/*/*.css","/*/*.js","/*/*.png","/*/*.jpeg");



    }
}
