package com.demo.rabbitmqdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置回调函数
 *
 * @className: RabbitConfig
 * @author: XuQiang Wang
 * @date: 2021/11/13
 **/
@Configuration
@Slf4j
public class RabbitConfig {
	
	
	@Bean
	public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate();
		rabbitTemplate.setConnectionFactory(connectionFactory);
		//设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
		rabbitTemplate.setMandatory(true);
		
		rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
			log.info("ConfirmCallback:     " + "相关数据：" + correlationData);
			log.info("ConfirmCallback:     " + "确认情况：" + ack);
			log.info("ConfirmCallback:     " + "原因：" + cause);
		});
		
		rabbitTemplate.setReturnsCallback(returned -> {
			log.info("ReturnCallback:     " + "消息：" + returned.getMessage());
			log.info("ReturnCallback:     " + "回应码：" + returned.getReplyCode());
			log.info("ReturnCallback:     " + "回应信息：" + returned.getReplyText());
			log.info("ReturnCallback:     " + "交换机：" + returned.getExchange());
			log.info("ReturnCallback:     " + "路由键：" + returned.getRoutingKey());
		});
		
		return rabbitTemplate;
	}
	
}
