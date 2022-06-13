package com.example.cscmp.config;

import com.example.cscmp.interceptor.SessionTimeoutInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;


@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    SessionTimeoutInterceptor sessionTimeoutInterceptor;

    /**
     * 释放静态资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/assets/ckeditor/**").
                addResourceLocations("classpath:/static/assets/ckeditor/").
                setCachePeriod(2592000);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/view").setViewName("graph-force");
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionTimeoutInterceptor).addPathPatterns("/**").
                excludePathPatterns("/**","/static/**");
    }


}
