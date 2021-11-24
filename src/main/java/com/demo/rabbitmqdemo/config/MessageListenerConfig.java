package com.demo.rabbitmqdemo.config;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 手动确认配置类
 *
 * @className: MessageListenerConfig
 * @author: XuQiang Wang
 * @date: 2021/11/13
 **/
@Configuration
public class MessageListenerConfig {
	
	
	@Bean
	public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) throws IOException {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		
		Channel channel = connectionFactory.createConnection().createChannel(false);
		//设置一个消费者只能处理一条消息
		channel.basicQos(1);
		factory.setConnectionFactory(connectionFactory);
		//开启手动 ack
		factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
		return factory;
	}
	
}
