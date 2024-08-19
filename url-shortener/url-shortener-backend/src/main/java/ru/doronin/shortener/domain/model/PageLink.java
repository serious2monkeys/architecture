package ru.doronin.shortener.domain.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Sharded;

import java.time.Instant;

@Value
@Document(collection = "pages")
@Sharded(immutableKey = true, shardKey = {"shortUrl", "longUrl"})
@CompoundIndex(name = "urls_index", def = "{'shortUrl': 1, 'longUrl': 1}", unique = true)
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