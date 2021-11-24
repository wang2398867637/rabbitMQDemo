package com.demo.rabbitmqdemo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 扇形交换机
 *
 * @className: FanoutRabbitConfig
 * @author: XuQiang Wang
 * @date: 2021/11/13
 **/
@Configuration
public class FanoutRabbitConfig {
	
	@Bean
	public Queue queueA() {
		return new Queue("fanout.A");
	}
	
	@Bean
	public Queue queueB() {
		return new Queue("fanout.B");
	}
	
	@Bean
	public Queue queueC() {
		return new Queue("fanout.C");
	}
	
	@Bean
	FanoutExchange fanoutExchange() {
		return new FanoutExchange("fanoutExchange");
	}
	
	@Bean
	Binding bindingExchangeA() {
		return BindingBuilder.bind(queueA()).to(fanoutExchange());
	}
	
	@Bean
	Binding bindingExchangeB() {
		return BindingBuilder.bind(queueB()).to(fanoutExchange());
	}
	
	@Bean
	Binding bindingExchangeC() {
		return BindingBuilder.bind(queueC()).to(fanoutExchange());
	}
	
	
}
