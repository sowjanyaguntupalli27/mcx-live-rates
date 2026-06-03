package com.vibullion.backend.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "com.vibullion.sms")
public class SmsProperties {
	private String url;
	private String appName;
}