package ru.doronin.shortener.domain.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Sharded;
import org.springframework.data.mongodb.core.mapping.ShardingStrategy;

import java.time.Instant;

@Value
@Document(collection = "pages")
@Sharded(shardingStrategy = ShardingStrategy.HASH, immutableKey = true, shardKey = {"shortUrl", "longUrl"})
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageLink {
    @Id
    Long id;
    @NotBlank
    String longUrl;
    @NotBlank
    String shortUrl;
    @Builder.Default
    long usageCounter = 0;
    @CreatedDate
    @Builder.Default
    Instant createTimestamp = Instant.now();
}