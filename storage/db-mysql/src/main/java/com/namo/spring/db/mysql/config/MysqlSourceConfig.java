package com.namo.spring.db.mysql.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MysqlSourceConfig {

	@Bean
	@ConfigurationProperties(prefix = "storage.datasource.mysql")
	public HikariConfig mysqlHikariConfig() {
		return new HikariConfig();
	}

	@Bean
	public HikariDataSource mysqlDataSource(
		@Qualifier("mysqlHikariConfig") HikariConfig config
	) {
		return new HikariDataSource(config);
	}

}
