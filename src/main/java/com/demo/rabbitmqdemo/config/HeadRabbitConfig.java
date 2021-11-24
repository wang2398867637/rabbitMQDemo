package com.demo.rabbitmqdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @className: HeadRabbitConfig
 * @author: XuQiang Wang
 * @date: 2021/11/22
 **/
@Configuration
@Slf4j
public class HeadRabbitConfig {
	
	
	@Bean
	public Queue headersQueue() {
		return new Queue("headersQueue");
	}
	
	@Bean
	public HeadersExchange headersExchange() {
		return new HeadersExchange("headersExchange");
	}
	
	@Bean
	Binding bindingExchangeTopicQueue2() {
		Map<String, Object> map = new HashMap<>();
		map.put("headsType", "A");
		
		//whereAll表示全部匹配
		return BindingBuilder.bind(headersQueue()).to(headersExchange()).whereAll(map).match();
		
		//whereAny表示部分匹配
		//return BindingBuilder.bind(queue).to(headersExchange()).whereAny(map).match();
	}
	
}
