package com.namo.spring.db.mysql.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.namo.spring.db.mysql.DomainPackageLocation;
import com.namo.spring.db.mysql.domains.JpaPackageLocation;

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EntityScan(basePackageClasses = DomainPackageLocation.class)
@EnableJpaRepositories(basePackageClasses = JpaPackageLocation.class)
public class JpaConfig {
}
