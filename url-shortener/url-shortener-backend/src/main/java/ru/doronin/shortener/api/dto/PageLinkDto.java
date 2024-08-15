package ru.doronin.shortener.api.dto;

import java.time.Instant;

/**
 * DTO for {@link ru.doronin.shortener.domain.model.PageLink}
 */
public record PageLinkDto(
        String longUrl,
        String shortUrl,
        Instant createTimestamp
) {
}