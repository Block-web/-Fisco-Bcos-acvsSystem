package com.zheng.acvsystem.config;

import com.zheng.acvsystem.interceptions.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//该类的主要作用为配置注册拦截器
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器                               excludePathPatterns不拦截   (录接口和注册接口)
        registry.addInterceptor(loginInterceptor).
                excludePathPatterns("/user/login", "/user/register");
    }

}
