package ru.doronin.shortener.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "shortener")
public class ApplicationProperties {
    private long nodeId = 1;
}
