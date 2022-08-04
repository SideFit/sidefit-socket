package com.project.sidefitrsocket.security;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenDto {
    private final String grantType;
    private final String accessToken;
    private final String refreshToken;
    private final Long accessTokenExpireDate;
}
