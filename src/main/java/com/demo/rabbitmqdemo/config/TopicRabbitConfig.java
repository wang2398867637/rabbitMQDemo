package com.demo.rabbitmqdemo.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 主题交换机配置类
 *
 * @className: TopicRabbitConfig
 * @author: XuQiang Wang
 * @date: 2021/11/13
 **/
@Configuration
public class TopicRabbitConfig {
	
	//绑定键
	public final static String producer1 = "mq.producer1";
	public final static String producer2 = "mq.producer2";
	public final static String producer3 = "user.producer1";
	
	@Bean
	public Queue firstQueue() {
		return new Queue(TopicRabbitConfig.producer1);
	}
	
	@Bean
	public Queue userFirstQueue() {
		return new Queue(TopicRabbitConfig.producer3);
	}
	
	@Bean
	public Queue secondQueue() {
		return new Queue(TopicRabbitConfig.producer2);
	}
	
	@Bean
	TopicExchange exchange() {
		return new TopicExchange("topicExchange");
	}
	
	
	//将firstQueue和topicExchange绑定,而且绑定的键值为mq.producer1
	//这样只要是消息携带的路由键是mq.producer1,才会分发到该队列
	@Bean
	Binding bindingExchangeMessage() {
		return BindingBuilder.bind(firstQueue()).to(exchange()).with(producer1);
	}
	
	
	@Bean
	Binding bindingUserExchangeMessage() {
		return BindingBuilder.bind(userFirstQueue()).to(exchange()).with(producer3);
	}
	
	//将secondQueue和topicExchange绑定,而且绑定的键值为用上通配路由键规则mq.#
	// 这样只要是消息携带的路由键是以mq.开头,都会分发到该队列
	@Bean
	Binding bindingExchangeMessage2() {
		return BindingBuilder.bind(secondQueue()).to(exchange()).with("mq.#");
	}
	
}
