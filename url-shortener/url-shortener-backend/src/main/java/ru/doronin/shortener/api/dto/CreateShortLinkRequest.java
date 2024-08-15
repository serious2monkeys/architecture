package ru.doronin.shortener.api.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record CreateShortLinkRequest(@NotBlank @URL String link) {
}
