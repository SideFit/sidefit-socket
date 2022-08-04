package com.project.sidefitrsocket.security;

import com.project.sidefitrsocket.properties.JwtProperties;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final String ROLES = "roles";
    private final JwtProperties jwtProperties;

    // Test 용 토큰 생성 용도.
    public TokenDto createTokenDto(Long userId, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
        claims.put(ROLES, roles);

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtProperties.getAccessTokenValidMillisecond()))
                .signWith(getKey(jwtProperties.getSecretKey()), SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setExpiration(new Date(now.getTime() + jwtProperties.getRefreshTokenValidMillisecond()))
                .signWith(getKey(jwtProperties.getSecretKey()), SignatureAlgorithm.HS256)
                .compact();

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireDate(jwtProperties.getAccessTokenValidMillisecond())
                .build();
    }

    public Long getAuthentication(String token) {
        return parseClaims(token).map(claims -> {
            log.info("userId: {}", claims.getSubject());
            return claims.getSubject();
        }).map(Long::valueOf)
        .orElse(0L);
    }

    private Optional<Claims> parseClaims(String token) {
        Claims body;
        try {
            body = Jwts.parserBuilder()
                    .setSigningKey(
                            getKey(jwtProperties.getSecretKey())
                    )
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            body = e.getClaims();
        }
        return Optional.of(body);
    }

    public Optional<Boolean> validationToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(
                            getKey(jwtProperties.getSecretKey())
                    ).build()
                    .parseClaimsJws(token);
            return Optional.of(true);
        } catch (Exception e) {
            log.error("jwt Error");
        }
        return Optional.of(false);
    }

    private Key getKey(String secretKey) {
        byte[] secretBytes = Base64.getEncoder().encode(secretKey.getBytes());
        return new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());
    }
}
