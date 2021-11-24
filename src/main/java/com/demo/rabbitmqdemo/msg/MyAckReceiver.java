package com.demo.rabbitmqdemo.msg;


import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息处理结果
 *
 * @className: MyAckReceiver
 * @author: XuQiang Wang
 * @date: 2021/11/13
 **/
@Component
@Slf4j
public class MyAckReceiver implements ChannelAwareMessageListener {
	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		
		String msg = message.toString();
		//可以点进Message里面看源码,单引号直接的数据就是我们的map消息数据
		String[] msgArray = msg.split("'");
		Map<String, String> msgMap = mapStringToMap(msgArray[1].trim(), 3);
		String messageContent = msgMap.get("messageContent");
		System.out.println("  MyAckReceiver : messageData:" + messageContent);
		System.out.println("消费的主题消息来自：" + message.getMessageProperties().getConsumerQueue());
		
		
	}
	
	
	//{key=value,key=value,key=value} 格式转换成map
	private Map<String, String> mapStringToMap(String str, int entryNum) {
		str = str.substring(1, str.length() - 1);
		String[] strs = str.split(",", entryNum);
		Map<String, String> map = new HashMap<String, String>();
		for (String string : strs) {
			String key = string.split("=")[0].trim();
			String value = string.split("=")[1];
			map.put(key, value);
		}
		return map;
	}
	
}
