package com.demo.rabbitmqdemo.config;

import com.demo.rabbitmqdemo.interceptor.WebMvcInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @className: MvcConfig
 * @author: XuQiang Wang
 * @date: 2021/11/15
 **/
@Configuration
public class WebMvcInterceptorConfig implements WebMvcConfigurer {
	
	@Bean
	public HandlerInterceptor webMvcInterceptor() {
		return new WebMvcInterceptor();
	}
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(webMvcInterceptor()).addPathPatterns("/**");
	}
}
