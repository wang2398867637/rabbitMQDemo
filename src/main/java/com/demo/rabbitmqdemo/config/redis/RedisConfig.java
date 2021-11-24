package com.demo.rabbitmqdemo.config.redis;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;

/**
 * +----------------------------------------------------------------------
 * | Redis配置组件
 * +----------------------------------------------------------------------
 */
@Configuration
@EnableCaching
public class RedisConfig {
	
	@Value("${spring.redis.host}")
	private String redisHost;
	
	@Value("${spring.redis.port}")
	private int redisPort;
	
	@Value("${spring.redis.password}")
	private String redisPass;
	
	@Value("${spring.redis.database}")
	private int redisDb;
	
	@Value("${spring.redis.timeout}")
	private String timeout;
	
	@Value("${spring.redis.jedis.pool.max-active}")
	private int maxTotal;
	
	@Value("${spring.redis.jedis.pool.max-idle}")
	private int maxIdle;
	
	@Value("${spring.redis.jedis.pool.max-wait}")
	private int maxWaitMillis;
	
	@Value("${spring.redis.jedis.pool.time-between-eviction-runs}")
	private String timeBetweenEvictionRunsMillis;
	
/*	@Bean
	@Primary
	public RedisConnectionFactory taskConnectionFactory() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		
		jedisPoolConfig.setTimeBetweenEvictionRunsMillis(Integer.parseInt(timeBetweenEvictionRunsMillis));
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory(jedisPoolConfig);
		
		
		return connectionFactory;
	}*/

    /*@Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        //设置序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        //配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);//key序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);//value序列化
        redisTemplate.setHashKeySerializer(stringSerializer);//Hash key序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);//Hash value序列化
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }*/
	
	@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		//JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
		GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
		FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
		// 设置值（value）的序列化采用FastJsonRedisSerializer。
		redisTemplate.setValueSerializer(fastJsonRedisSerializer);
		//redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
		// 设置键（key）的序列化采用StringRedisSerializer。
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new StringRedisSerializer());
		redisTemplate.afterPropertiesSet();
		// 设置hash key 和value序列化模式
		return redisTemplate;
	}
	
	
	@Bean(destroyMethod = "shutdown")
	public RedissonClient redisson() throws IOException {
		RedissonClient redisson = Redisson.create(
				Config.fromYAML(new ClassPathResource("redisson-single.yml").getInputStream()));
		return redisson;
	}
	
}


