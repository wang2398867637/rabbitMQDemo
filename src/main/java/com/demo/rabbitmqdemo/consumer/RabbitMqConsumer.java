
package com.demo.rabbitmqdemo.consumer;

import com.rabbitmq.client.AMQP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;


/**
 * 消费者
 *
 * @className: RabbitMqConsumer
 * @author: XuQiang Wang
 * @date: 2021/10/18
 **/

@Slf4j
@Component

public class RabbitMqConsumer {
	
	
	@RabbitHandler
	@RabbitListener(queues = "TestDirectQueue")
	public void process(Map testMessage, Channel channel, @Headers Map<String, Object> map) {
		log.info("DirectReceiver消费者收到消息  : " + testMessage.toString());
		
		try {
			log.info("DirectReceiver消费者确认");
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
	
	
	//监听器监听指定的Queue
	@RabbitListener(queues = "headersQueue")
	@RabbitHandler
	public void headers(Map testMessage, Channel channel, @Headers Map<String, Object> map) {
		log.info("headers消费者收到消息  : " + testMessage.toString());
		log.info((String) map.get("headsType"));
		try {
			log.info("headers消费者确认");
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

