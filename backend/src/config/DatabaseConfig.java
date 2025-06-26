package com.myapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.myapp.repositories")
public class DatabaseConfig {
    // Configurações adicionais para o SQL Server podem ser definidas no application.properties
}
