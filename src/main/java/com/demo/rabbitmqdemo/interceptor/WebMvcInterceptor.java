package com.demo.rabbitmqdemo.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.demo.util.ThreadLocalUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 设置拦截器
 *
 * @className: WebMvcInterceptor
 * @author: XuQiang Wang
 * @date: 2021/11/15
 **/
public class WebMvcInterceptor implements HandlerInterceptor {
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		//交换机名称
		String exchangeName = request.getHeader("exchangeName");
		//请求路由
		String routingKey = request.getHeader("routingKey");
		
		if (StringUtils.isAllBlank(exchangeName)) {
			response.setStatus(500);
			return false;
		}
		
		ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<>();
		JSONObject object = new JSONObject();
		object.put("exchangeName", exchangeName);
		object.put("routingKey", routingKey);
		hashMap.put("info", object);
		//请求头获取heads模式信息
		if (StringUtils.isNotBlank(request.getHeader("headsKey"))) {
			object.put("headsKey", request.getHeader("headsKey"));
		}
		ThreadLocalUtil.set(hashMap);
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
