package ru.doronin.shortener.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class LinkNotFoundException extends RuntimeException {
    String link;

    public LinkNotFoundException(String link) {
        super("Page not found: ".concat(link));
        this.link = link;
    }
}
