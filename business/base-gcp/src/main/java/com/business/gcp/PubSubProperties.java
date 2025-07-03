package com.business.gcp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Provides a default application.properties in base-gcp
 * gcp.pubsub.project-id=YOUR_PROJECT_ID
 * gcp.pubsub.encoded-key=
 */

@Configuration
@ConfigurationProperties(prefix = "gcp.pubsub")
@Data
public class PubSubProperties {
    private String projectId;
    private String encodedKey;
}

