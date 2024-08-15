package ru.doronin.shortener.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.doronin.Snowflake;
import ru.doronin.shortener.domain.model.PageLink;
import ru.doronin.shortener.domain.repository.PageLinkRepository;
import ru.doronin.shortener.utils.Base62;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PageLinkService {
    private final PageLinkRepository pageLinkRepository;
    private final Snowflake snowflakeGenerator;

    public PageLink createOrGetShortLink(String longLink) {
        return pageLinkRepository.findPageLinkByLongUrl(longLink)
                .orElseGet(() -> {
                    var id = snowflakeGenerator.nextId();
                    var shortLink = PageLink.builder()
                            .id(id)
                            .shortUrl(Base62.encode(id))
                            .longUrl(longLink)
                            .build();
                    return pageLinkRepository.save(shortLink);
                });
    }

    public Optional<String> getLongLinkByShort(String shortLink) {
        var linkId = Base62.decode(shortLink);
        return pageLinkRepository.findById(linkId)
                .map(link -> {
                    pageLinkRepository.findAndIncrementPageLinkById(linkId);
                    return link.getLongUrl();
                });
    }
}
