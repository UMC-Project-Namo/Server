package com.namo.spring.db.mysql.config;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@ActiveProfiles("test")
public class MysqlTestContainerConfig {
	private static final String MYSQL_CONTAINER_IMAGE = "mysql:8.0.26";
	private static final MySQLContainer<?> MY_SQL_CONTAINER;

	static {
		MY_SQL_CONTAINER =
			new MySQLContainer<>(DockerImageName.parse(MYSQL_CONTAINER_IMAGE))
				.withDatabaseName("namo_test")
				.withUsername("namo")
				.withPassword("testpass")
				.withReuse(true);

		MY_SQL_CONTAINER.start();
	}

	@DynamicPropertySource
	public static void setMysqlProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url",
			() -> String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC&characterEncoding=utf8",
				MY_SQL_CONTAINER.getHost(),
				MY_SQL_CONTAINER.getFirstMappedPort(),
				MY_SQL_CONTAINER.getDatabaseName()
			));
		registry.add("spring.datasource.username", () -> "namo");
		registry.add("spring.datasource.password", () -> "testpass");
	}
}
