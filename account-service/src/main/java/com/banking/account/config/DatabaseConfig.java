package com.banking.account.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.banking.account.repository")
@EnableJpaAuditing
@EnableConfigurationProperties(AppProperties.class)
public class DatabaseConfig {
}

