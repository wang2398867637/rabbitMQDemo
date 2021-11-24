package com.demo.rabbitmqdemo.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 扇形消费者
 *
 * @className: RabbitMqFanoutConsumer
 * @author: XuQiang Wang
 * @date: 2021/11/13
 */

@Component
@Slf4j
public class RabbitMqFanoutConsumer {
	
	@RabbitListener(queues = "fanout.A")
	@RabbitHandler
	public void processA(Map testMessage, Channel channel, @Headers Map<String, Object> map) {
		log.info("扇形消费者A  : " + testMessage.toString());
		
		try {
			log.info("扇形消费者A确认");
			//确认消息
			channel.basicAck((Long) map.get(AmqpHeaders.DELIVERY_TAG), false);
		} catch (IOException e) {
			log.error("异常拒绝");
			try {
				channel.basicReject((Long) map.get(AmqpHeaders.DELIVERY_TAG), true);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
		}
		
	}
	
	@RabbitListener(queues = "fanout.B")
	@RabbitHandler
	public void processB(Map testMessage, Channel channel, @Headers Map<String, Object> map) {
		log.info("扇形消费者B  : " + testMessage.toString());
		
		try {
			log.info("扇形消费者B确认");
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
	
	@RabbitListener(queues = "fanout.C")
	@RabbitHandler
	public void processC(Map testMessage, Channel channel, @Headers Map<String, Object> map) {
		log.info("扇形消费者C  : " + testMessage.toString());
		
		
		try {
			log.info("扇形消费者C确认");
			//确认消息
			channel.basicAck((Long) map.get(AmqpHeaders.DELIVERY_TAG), false);
		} catch (IOException e) {
			log.error("异常拒绝");
			try {
				channel.basicReject((Long) map.get(AmqpHeaders.DELIVERY_TAG), true);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
		}
	}
}
