package com.namo.spring.db.redis.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.namo.spring.db.redis.common.annotation.DomainRedisCacheManager;
import com.namo.spring.db.redis.common.annotation.DomainRedisConnectionFactory;
import com.namo.spring.db.redis.common.annotation.DomainRedisTemplate;
import com.namo.spring.db.redis.cache.RedisPackageLocation;

@Configuration
@EnableRedisRepositories(basePackageClasses = RedisPackageLocation.class)
@EnableTransactionManagement
public class RedisConfig {
	private final String redisHost;
	private final int redisPort;

	public RedisConfig(
		@Value("${cloud.redis.host}") String redisHost,
		@Value("${cloud.redis.port}") int redisPort
	) {
		this.redisHost = redisHost;
		this.redisPort = redisPort;
	}

	@Bean
	@DomainRedisConnectionFactory
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
		LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder().build();

		return new LettuceConnectionFactory(config, clientConfig);
	}

	@Bean
	@Primary
	@DomainRedisTemplate
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

		redisTemplate.setConnectionFactory(redisConnectionFactory());

		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		return redisTemplate;
	}

	@Bean
	@DomainRedisCacheManager
	public RedisCacheManager redisCacheManager(@DomainRedisConnectionFactory RedisConnectionFactory cf) {
		RedisCacheConfiguration cacheConfiguration =
			RedisCacheConfiguration.defaultCacheConfig()
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
				.entryTtl(Duration.ofHours(3L));

		return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(cf)
			.cacheDefaults(cacheConfiguration)
			.build();
	}
}
