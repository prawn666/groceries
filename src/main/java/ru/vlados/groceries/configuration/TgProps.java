package ru.vlados.groceries.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "tg")
public class TgProps {

    private String webhookUrl;
    private String token;
    private RunMode runMode;
    private String languageCode="en";

    private enum RunMode {
        LONG_POLL,
        WEBHOOK
    }
}
