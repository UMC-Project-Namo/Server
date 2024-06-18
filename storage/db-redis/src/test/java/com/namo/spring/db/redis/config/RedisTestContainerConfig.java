package com.namo.spring.db.redis.config;

import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@DisplayName("Redis Test용 Container 설정")
public class RedisTestContainerConfig {
	private static final String REDIS_CONTAINER_NAME = "redis:7.2.4-alpine";
	private static final String REDIS_PORT = "6379";
	private static final GenericContainer<?> REDIS_CONTAINER;

	static {
		REDIS_CONTAINER =
			new GenericContainer<>(DockerImageName.parse(REDIS_CONTAINER_NAME))
				.withExposedPorts(Integer.parseInt(REDIS_PORT))
				.withReuse(true);

		REDIS_CONTAINER.start();
	}

	@DynamicPropertySource
	public static void setRedisProperties(DynamicPropertyRegistry registry) {
		registry.add("cloud.aws.redis.host", REDIS_CONTAINER::getHost);
		registry.add("cloud.aws.redis.port", () -> REDIS_CONTAINER.getMappedPort(Integer.parseInt(REDIS_PORT)));
	}
}
