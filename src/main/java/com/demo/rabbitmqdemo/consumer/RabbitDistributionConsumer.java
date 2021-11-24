package com.demo.rabbitmqdemo.consumer;

import com.demo.util.RedisUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ReturnListener;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 分发消费者
 *
 * @className: RabbitDistributionConsumer
 * @author: XuQiang Wang
 * @date: 2021/11/15
 **/
@Component
@Slf4j
public class RabbitDistributionConsumer {
	
	private final String queue = "distributionQueue";
	
	
	@Resource
	private RedissonClient redissonClient;
	
	@Resource(name = "taskExecutor")
	private ThreadPoolTaskExecutor taskExecutor;
	
	private final ReentrantLock lock = new ReentrantLock();
	Condition condition = lock.newCondition();
	
	@RabbitHandler
	@RabbitListener(queues = queue)
	public void process(Map testMessage, Channel channel, @Headers Map<String, Object> map) {
		log.info("分发一号  : " + testMessage.toString());
		
		CyclicBarrier barrier = new CyclicBarrier(9);
		
		for (int i = 1; i <= 3; i++) {
			int finalI = i;
			taskExecutor.execute(() -> {
				boolean store = this.decrementProductStore("一号" + finalI);
				
				if (!store) {
					log.error("一号" + finalI + "失败");
					this.decrementProductStore("一号" + finalI);
				}
				try {
					
					barrier.await();
					
				} catch (InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}
			});
		}
		
		try {
			barrier.await();
			//确认消息
			channel.basicAck((Long) map.get(AmqpHeaders.DELIVERY_TAG), false);
			log.info("分发一号确认");
		} catch (Exception e) {
			log.error("异常拒绝" + e.getMessage());
			try {
				channel.basicReject((Long) map.get(AmqpHeaders.DELIVERY_TAG), true);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	
	@RabbitHandler
	@RabbitListener(queues = queue)
	public void process1(Map testMessage, Channel channel, @Headers Map<String, Object> map) {
		log.info("分发二号  : " + testMessage.toString());
		
		try {
			
			boolean store = this.decrementProductStore("二号");
			if (!store) {
				throw new Exception("分布式锁获取失败");
			}
			//确认消息
			channel.basicAck((Long) map.get(AmqpHeaders.DELIVERY_TAG), false);
			log.info("分发二号确认");
		} catch (Exception e) {
			log.error("异常拒绝" + e.getMessage());
			try {
				channel.basicReject((Long) map.get(AmqpHeaders.DELIVERY_TAG), true);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
		}
	}
	
	@RabbitHandler
	@RabbitListener(queues = queue)
	public void process2(Map testMessage, Channel channel, @Headers Map<String, Object> map) {
		log.info("分发三号  : " + testMessage.toString());
		
		try {
			
			boolean store = this.decrementProductStore("三号");
			if (!store) {
				throw new Exception("分布式锁获取失败");
			}
			//确认消息
			channel.basicAck((Long) map.get(AmqpHeaders.DELIVERY_TAG), false);
			log.info("分发三号确认");
		} catch (Exception e) {
			log.error("异常拒绝" + e.getMessage());
			try {
				channel.basicReject((Long) map.get(AmqpHeaders.DELIVERY_TAG), true);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
		}
	}
	
	@RabbitHandler
	@RabbitListener(queues = queue)
	public void process3(Map testMessage, Channel channel, @Headers Map<String, Object> map) {
		log.info("分发四号  : " + testMessage.toString());
		
		try {
			
			boolean store = this.decrementProductStore("四号");
			if (!store) {
				throw new Exception("分布式锁获取失败");
			}
			//确认消息
			channel.basicAck((Long) map.get(AmqpHeaders.DELIVERY_TAG), false);
			log.info("分发四号确认");
		} catch (Exception e) {
			log.error("异常拒绝" + e.getMessage());
			try {
				channel.basicReject((Long) map.get(AmqpHeaders.DELIVERY_TAG), true);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
		}
	}
	
	
	private boolean decrementProductStore(String name) {
		
		log.info("进入方法=======" + name + "  :");
		String key = "redis:lock:_A";
		RLock redisLock = redissonClient.getLock(key);
		
		lock.lock();
		try {
			
			log.info(name + " 准备获取锁");
			
			while (redisLock.isLocked()) {
				log.error(name + "失败,锁定中,等待" + Thread.currentThread().getName());
				condition.await(1 ,TimeUnit.SECONDS);
			}
			
			//加锁时间,到期时长,时间单位
			while (!redisLock.tryLock(400, 500, TimeUnit.MILLISECONDS)) {
				log.info("等待获取锁");
				condition.await(100 ,TimeUnit.MILLISECONDS);
			}
			
			log.info(name + " 拿到锁");
			
			TimeUnit.MILLISECONDS.sleep(300);
			
			log.info(name + " 运行结束");
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			
			//分布式锁解锁
			redisLock.unlock();
			log.info(name + "释放锁");
			
			condition.signalAll();
			
			lock.unlock();
			log.info("======");
		}
	}
}
