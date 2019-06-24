package io.github.lr.ip2country.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Bean de configuracion de cache.
 * 
 * @author lravanal
 *
 */
@EnableCaching
@Configuration
public class CacheConfiguration {
	
	@Value("${redis.hostname}") private String hostname;
	
	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		return new JedisConnectionFactory(new RedisStandaloneConfiguration(hostname));
	}
	 
	@Bean
	public RedisTemplate<String, Long> redisTemplate() {
	    RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(RedisSerializer.string());
		redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
	    return redisTemplate;
	}
	
}
