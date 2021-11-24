
package com.demo.rabbitmqdemo.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 主题交换机消费者
 *
 * @className: TopicManReceiver
 * @author: XuQiang Wang
 * @date: 2021/11/13
 **/


@Component
@Slf4j
public class RabbitMqTopicConsumer {
	@RabbitHandler
	@RabbitListener(queues = "mq.producer2")
	public void process2(Map testMessage, Channel channel, @Headers Map<String, Object> map) {
		log.info("消费者2收到消息  : " + testMessage.toString());
		
		
		
		try {
			log.info("消费者2确认");
			//确认消息
			channel.basicAck((Long) map.get(AmqpHeaders.DELIVERY_TAG), false);
		} catch (IOException e) {
			log.error("异常拒绝");
			try {
				channel.basicReject((Long) map.get(AmqpHeaders.DELIVERY_TAG), false);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
		}
	}
	
	
	@RabbitHandler
	@RabbitListener(queues = "mq.producer1")
	public void process1(Map testMessage, Channel channel, @Headers Map<String, Object> map) {
		log.info("消费者1收到消息  : " + testMessage.toString());
		try {
			log.info("消费者1确认");
			//确认消息
			channel.basicAck((Long) map.get(AmqpHeaders.DELIVERY_TAG), false);
		} catch (IOException e) {
			log.error("异常拒绝");
			try {
				channel.basicReject((Long) map.get(AmqpHeaders.DELIVERY_TAG), false);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
		}
	}
	
	
	@RabbitHandler
	@RabbitListener(queues = "user.producer1")
	public void userProcess1(Map testMessage, Channel channel, @Headers Map<String, Object> map) {
		log.info("消费者User收到消息  : " + testMessage.toString());
		
		try {
			log.info("消费者User确认");
			//确认消息
			channel.basicAck((Long) map.get(AmqpHeaders.DELIVERY_TAG), false);
		} catch (IOException e) {
			log.error("异常拒绝");
			try {
				channel.basicReject((Long) map.get(AmqpHeaders.DELIVERY_TAG), false);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
		}
	}
}

