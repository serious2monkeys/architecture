package ru.doronin.shortener.domain.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;
import ru.doronin.shortener.domain.model.PageLink;

import java.util.Optional;

@Repository
public interface PageLinkRepository extends MongoRepository<PageLink, Long> {
    Optional<PageLink> findPageLinkByShortUrl(String shortUrl);
    Optional<PageLink> findPageLinkByLongUrl(String longUrl);

    @Update("{'$inc': {'usageCounter': 1}}")
    void findAndIncrementPageLinkById(Long id);
}