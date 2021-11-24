/*
package com.demo.rabbitmqdemo.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

*/
/**
 * @className: RabbitMqUtils
 * @author: XuQiang Wang
 * @date: 2021/10/20
 **//*

@Slf4j
public class RabbitMqUtils {
	
	public static Channel get() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		//链接队列
		factory.setHost("127.0.0.1");
		//用户名
		factory.setUsername("admin");
		//账户密码
		factory.setPassword("123");
		//创建链接
		Connection connection = factory.newConnection();
		//获取信道
		return connection.createChannel();
	}
	
}
*/
