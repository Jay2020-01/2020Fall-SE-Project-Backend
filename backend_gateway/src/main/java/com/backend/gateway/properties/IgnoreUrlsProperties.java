package com.backend.gateway.properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnExpression("!'${ignore}'.isEmpty()")
@ConfigurationProperties(prefix = "ignore")
public class IgnoreUrlsProperties {
    private List<String> httpUrls = new ArrayList<>();

    public List<String> getHttpUrls() {
        return httpUrls;
    }

    public void setHttpUrls(List<String> httpUrls) {
        this.httpUrls = httpUrls;
    }
}
