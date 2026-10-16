/** DTO representing the DvelopApiProperties data structure received from the D-velop document management webhook. */
package com.rex.commodis.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "dvelop.api")
public class DvelopApiProperties {

    private String baseUrl;
    private String transfersPath;
    private String integrationKey;
}