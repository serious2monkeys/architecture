package ru.doronin.shortener.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class WrongLinkException extends RuntimeException {
    String link;

    public WrongLinkException(String link) {
        super("Wrong URL: ".concat(link));
       this.link = link;
    }
}
