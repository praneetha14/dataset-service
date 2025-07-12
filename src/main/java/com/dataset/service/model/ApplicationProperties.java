package com.dataset.service.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "com.dataset.service")
public class ApplicationProperties {
    private String baseUrl;
}
