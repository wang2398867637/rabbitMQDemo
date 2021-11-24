package com.demo.rabbitmqdemo.producer;

import com.alibaba.fastjson.JSON;
import com.demo.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 生产者
 *
 * @className: RabbitMq
 * @author: XuQiang Wang
 * @date: 2021/10/16
 **/
@Slf4j
@Service
public class RabbitMqProducerService {
	
	@Value("${rabbit.direct.name}")
	private String rabbitDirectName;
	
	@Value("${rabbit.direct.routing}")
	private String rabbitDirectRouting;
	
	@Resource
	private RabbitTemplate rabbitTemplate;
	
	/**
	 * 普通分发消息
	 *
	 * @param msg
	 * @return java.lang.Boolean
	 * @author XuQiang Wang
	 * @date 2021/11/13
	 */
	public Boolean getProducer(String msg) {
		ConcurrentHashMap<String, Object> messageMap = new ConcurrentHashMap<>();
		messageMap.put("messageContent", msg);
		Map<String, String> parseObject = ThreadLocalUtil.getInfoMap("info");
		rabbitTemplate.convertAndSend(parseObject.get("exchangeName"), parseObject.get("routingKey"), messageMap);
		return true;
	}
	
	
	/**
	 * 主题分发生产者1
	 *
	 * @param msg
	 * @return java.lang.Boolean
	 * @author XuQiang Wang
	 * @date 2021/11/13
	 */
	public Boolean getTopicProducer(String msg) {
		ConcurrentHashMap<String, Object> messageMap = new ConcurrentHashMap<>();
		messageMap.put("TopicContent", msg);
		Map<String, String> parseObject = ThreadLocalUtil.getInfoMap("info");
		rabbitTemplate.convertAndSend(parseObject.get("exchangeName"), parseObject.get("routingKey"), messageMap);
		return true;
	}
	
	/**
	 * 扇形分发生产者1
	 *
	 * @param msg
	 * @return java.lang.Boolean
	 * @author XuQiang Wang
	 * @date 2021/11/13
	 */
	public Boolean getFanoutProducer(String msg) {
		//发送的消息
		ConcurrentHashMap<String, Object> messageMap = new ConcurrentHashMap<>();
		messageMap.put("FanoutProducerContent", msg);
		
		Map<String, String> parseObject = ThreadLocalUtil.getInfoMap("info");
		rabbitTemplate.convertAndSend(parseObject.get("exchangeName"), parseObject.get("routingKey"), messageMap);
		return true;
	}
	
	/**
	 * 分发
	 *
	 * @param msg
	 * @return java.lang.Boolean
	 * @author XuQiang Wang
	 * @date 2021/11/15
	 */
	public Boolean distributionProducer(String msg) {
		//发送的消息
		ConcurrentHashMap<String, Object> messageMap = new ConcurrentHashMap<>();
		messageMap.put("distributionProducerContent", msg);
		
		Map<String, String> parseObject = ThreadLocalUtil.getInfoMap("info");
		rabbitTemplate.convertAndSend(parseObject.get("exchangeName"), parseObject.get("routingKey"), messageMap);
		return true;
	}
	
	/**
	 * heads模式
	 *
	 * @param msg
	 * @return java.lang.Boolean
	 * @author XuQiang Wang
	 * @date 2021/11/22
	 */
	public Boolean headsProducer(String msg) {
		//发送的消息
		ConcurrentHashMap<String, Object> messageMap = new ConcurrentHashMap<>();
		messageMap.put("headsContent", msg);
		Map<String, String> parseObject = ThreadLocalUtil.getInfoMap("info");
		
		if (StringUtils.isBlank(parseObject.get("headsKey"))) {
			return false;
		}
		/**
		 * 声明消息 (消息体, 消息属性)
		 */
		MessageProperties messageProperties = new MessageProperties();
		HashMap<String, Object> headsMap = new HashMap<>();
		headsMap.put("headsType", parseObject.get("headsKey"));
		
		// 设置消息是否持久化。Persistent表示持久化，Non-persistent表示不持久化
		messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
		messageProperties.setContentType("UTF-8");
		messageProperties.getHeaders().putAll(headsMap);
		
		//序列化
		SimpleMessageConverter converter = new SimpleMessageConverter();
		Message message = converter.toMessage(messageMap, messageProperties);
		
		rabbitTemplate.convertAndSend(parseObject.get("exchangeName"), parseObject.get("routingKey"), message);
		return true;
	}
	
	
}
