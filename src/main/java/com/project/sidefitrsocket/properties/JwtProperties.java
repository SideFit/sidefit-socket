package com.project.sidefitrsocket.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("jwt")
public class JwtProperties {
    private String secretKey;
    private Long accessTokenValidMillisecond; // 1 hour
    private Long refreshTokenValidMillisecond; // 14 day
}
