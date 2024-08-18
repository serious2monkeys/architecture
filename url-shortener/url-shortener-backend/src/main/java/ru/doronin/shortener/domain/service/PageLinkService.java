package ru.doronin.shortener.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import ru.doronin.Snowflake;
import ru.doronin.shortener.domain.model.PageLink;
import ru.doronin.shortener.domain.repository.PageLinkRepository;
import ru.doronin.shortener.utils.Base62;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PageLinkService {
    private static final String SHORT_LINKS_CACHE_NAME = "shortLinksCache";
    private static final String LONG_LINKS_CACHE_NAME = "longLinksCache";
    private final PageLinkRepository pageLinkRepository;
    private final Snowflake snowflakeGenerator;
    private final CacheManager cacheManager;

    public PageLink createOrGetShortLink(String longLink) {
        return getFromCacheByLongLink(longLink)
                .orElseGet(() -> {
                    var pageLink = pageLinkRepository.findPageLinkByLongUrl(longLink)
                            .orElseGet(() -> {
                                var id = snowflakeGenerator.nextId();
                                var shortLink = PageLink.builder()
                                        .id(id)
                                        .shortUrl(Base62.encode(id))
                                        .longUrl(longLink)
                                        .build();
                                return pageLinkRepository.save(shortLink);
                            });
                    updateCaches(pageLink);
                    return pageLink;
                });
    }

    private void updateCaches(PageLink pageLink) {
        Optional.ofNullable(cacheManager.getCache(SHORT_LINKS_CACHE_NAME))
                .ifPresent(cache -> cache.put(pageLink.getShortUrl(), pageLink));
        Optional.ofNullable(cacheManager.getCache(LONG_LINKS_CACHE_NAME))
                .ifPresent(cache -> cache.put(pageLink.getLongUrl(), pageLink));
    }

    private Optional<PageLink> getFromCacheByLongLink(String longLink) {
        return getFromCache(longLink, LONG_LINKS_CACHE_NAME);
    }

    private Optional<PageLink> getFromCacheByShortLink(String shortLink) {
        return getFromCache(shortLink, SHORT_LINKS_CACHE_NAME);
    }

    private Optional<PageLink> getFromCache(String key, String cacheName) {
        var cache = cacheManager.getCache(cacheName);
        return (cache != null) ? Optional.ofNullable(cache.get(key, PageLink.class)) : Optional.empty();
    }

    public Optional<String> getLongLinkByShort(String shortLink) {
        var linkId = Base62.decode(shortLink);
        return getFromCacheByShortLink(shortLink)
                .or(() -> pageLinkRepository.findById(linkId)
                        .map(link -> {
                            updateCaches(link);
                            return link;
                        })
                )
                .map(pageLink -> {
                    pageLinkRepository.findAndIncrementPageLinkById(linkId);
                    return pageLink.getLongUrl();
                });
    }
}
