package ru.doronin.shortener.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ru.doronin.Snowflake;
import ru.doronin.shortener.config.properties.ApplicationProperties;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "ru.doronin.shortener.domain.repository")
@RequiredArgsConstructor
public class ApplicationConfig {
    private final ApplicationProperties applicationProperties;

    @Bean
    public Snowflake snowflakeGenerator() {
        return new Snowflake(applicationProperties.getNodeId());
    }
}
