package ru.doronin.shortener.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.doronin.shortener.api.dto.CommonErrorResponse;
import ru.doronin.shortener.api.dto.CreateShortLinkRequest;
import ru.doronin.shortener.api.dto.PageLinkDto;
import ru.doronin.shortener.domain.service.PageLinkService;
import ru.doronin.shortener.exception.LinkNotFoundException;
import ru.doronin.shortener.exception.WrongLinkException;

import java.net.URI;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class LinksController {
    private final PageLinkService pageLinkService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PageLinkDto createShortLink(@RequestBody @Valid CreateShortLinkRequest request) {
        var storedLink = pageLinkService.createOrGetShortLink(request.link());
        return new PageLinkDto(storedLink.getLongUrl(), storedLink.getShortUrl(), storedLink.getCreateTimestamp());
    }

    @GetMapping("/{shortLink}")
    public ResponseEntity<Void> redirect(@PathVariable String shortLink, HttpServletRequest request) {
        return pageLinkService.getLongLinkByShort(shortLink).map(storedLink ->
            ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).location(URI.create(storedLink)).<Void>build()
        ).orElseThrow(() -> new LinkNotFoundException(request.getRequestURI()));
    }

    @ExceptionHandler({WrongLinkException.class, LinkNotFoundException.class})
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    CommonErrorResponse handle(Exception exception) {
        return new CommonErrorResponse(exception.getMessage());
    }

}

