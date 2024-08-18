package ru.doronin.shortener.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import ru.doronin.Snowflake;
import ru.doronin.shortener.config.properties.ApplicationProperties;
import ru.doronin.shortener.domain.model.PageLink;

import java.time.Duration;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.IGNORE_UNKNOWN;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@Configuration
@EnableCaching
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "ru.doronin.shortener.domain.repository")
@RequiredArgsConstructor
public class ApplicationConfig {
    private final ApplicationProperties applicationProperties;

    @Bean
    public Snowflake snowflakeGenerator() {
        return new Snowflake(applicationProperties.getNodeId());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .findAndAddModules()
                .enable(IGNORE_UNKNOWN)
                .disable(FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer cacheManagerBuilderCustomizer() {
        return builder -> builder
                .withCacheConfiguration("longLinksCache",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .disableCachingNullValues()
                                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(objectMapper(), PageLink.class)))
                                .entryTtl(Duration.ofHours(1)))
                .withCacheConfiguration("shortLinksCache",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .disableCachingNullValues()
                                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(objectMapper(), PageLink.class)))
                                .entryTtl(Duration.ofMinutes(5)));
    }
}
