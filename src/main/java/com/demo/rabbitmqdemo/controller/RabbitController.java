package com.demo.rabbitmqdemo.controller;

import com.demo.rabbitmqdemo.producer.RabbitMqProducerService;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @className: RabbitController
 * @author: XuQiang Wang
 * @date: 2021/10/18
 **/
@RestController
@RequestMapping("mq/get")
public class RabbitController {
	
	/**
	 * 生产者
	 */
	@Resource
	private RabbitMqProducerService producer;
	
	
	@PostMapping("producer")
	public String getProducer(String message) {
		if (this.producer.getProducer(message)) {
			return "发送消息成功";
		} else {
			return "发送消息失败";
		}
	}
	
	@PostMapping("producer/topic")
	public String getTopicProducer(String message) {
		if (this.producer.getTopicProducer(message)) {
			return "发送消息成功";
		} else {
			return "发送消息失败";
		}
	}
	
	
	@PostMapping("producer/fanout")
	public String getFanoutProducer(String message) {
		if (this.producer.getFanoutProducer(message)) {
			return "发送消息成功";
		} else {
			return "发送消息失败";
		}
	}
	
	@PostMapping("producer/distribution")
	public String distributionProducer(String message) {
		if (this.producer.distributionProducer(message)) {
			return "发送消息成功";
		} else {
			return "发送消息失败";
		}
	}
	
	@PostMapping("producer/heads")
	public String headsProducer(String message) {
		if (this.producer.headsProducer(message)) {
			return "发送消息成功";
		} else {
			return "发送消息失败";
		}
	}
	
	
}
