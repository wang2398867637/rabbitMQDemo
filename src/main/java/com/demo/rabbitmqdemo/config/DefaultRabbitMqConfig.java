package com.demo.rabbitmqdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @className: DefaultRabbitMqConfig
 * @author: XuQiang Wang
 * @date: 2021/11/15
 **/
@Slf4j
@Configuration
public class DefaultRabbitMqConfig {
	
	
	@Bean
	public Queue distributionQueue() {
		return new Queue("distributionQueue");
	}
	
	@Bean
	DirectExchange distributionExchange() {
		return new DirectExchange("distributionExchange");
	}
	
	@Bean
	public Binding binding() {
		return BindingBuilder.bind(distributionQueue()).to(distributionExchange()).with("distribution");
	}
	
	@Bean
	public Binding binding2() {
		return BindingBuilder.bind(distributionQueue()).to(distributionExchange()).with("distribution");
	}
	
	;
}
