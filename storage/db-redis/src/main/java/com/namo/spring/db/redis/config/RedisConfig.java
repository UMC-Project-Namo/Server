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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(customObjectMapper()));
		return redisTemplate;
	}

	@Bean
	@DomainRedisCacheManager
	public RedisCacheManager redisCacheManager(@DomainRedisConnectionFactory RedisConnectionFactory cf) {
		RedisCacheConfiguration cacheConfiguration =
			RedisCacheConfiguration.defaultCacheConfig()
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(customObjectMapper())))
				.entryTtl(Duration.ofHours(3L));

		return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(cf)
			.cacheDefaults(cacheConfiguration)
			.build();
	}


    @Bean
    public ObjectMapper customObjectMapper(){
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator
                .builder()
                .allowIfSubType(Object.class)
                .build();

        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) // 혹시라도 의도치 않거나 알 수 없는 정보가 들어와 시리얼라이즈를 할 수 없게 될 경우를 대비한 설정값
                .registerModule(new JavaTimeModule())
                .activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL)
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS); // redis를 활용할 객체들에 날짜 정보가 TimeStamp 형식으로 적용되어있을 경우 그대로 RedisTemplate을 사용하면 에러가 발생하므로 그것에 대비하기 위한 설정값
    }
    
}
