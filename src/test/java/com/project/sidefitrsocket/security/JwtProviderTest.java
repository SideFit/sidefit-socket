package com.project.sidefitrsocket.security;

import com.project.sidefitrsocket.properties.JwtProperties;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class JwtProviderTest {
    private final JwtProperties jwtProperties = JwtProperties.builder()
            .secretKey("aegregaerfawefawfawgawe")
            .accessTokenValidMillisecond(10000000L)
            .refreshTokenValidMillisecond(100000000000L)
            .build();

    private final JwtProvider jwtProvider = new JwtProvider(jwtProperties);

    @Test
    void createTokenDtoTest() {
        Long userId = 1L;
        List<String> roles = List.of("USER");

        TokenDto tokenDto = jwtProvider.createTokenDto(userId, roles);

        System.out.println(tokenDto.getAccessToken());

        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());
        assertNotNull(tokenDto.getAccessTokenExpireDate());
    }

    @Test
    void getAuthenticationTest() {
        Long userId = 1L;
        List<String> roles = List.of("USER");
        TokenDto tokenDto = jwtProvider.createTokenDto(userId, roles);

        assertEquals(1, (long) jwtProvider.getAuthentication(tokenDto.getAccessToken()));
        assertEquals(1, (long) jwtProvider.getAuthentication(tokenDto.getRefreshToken()));
    }
    @Test
    void validationTokenTest() {
        Long userId = 1L;
        List<String> roles = List.of("USER");
        TokenDto tokenDto = jwtProvider.createTokenDto(userId, roles);

        assertTrue(jwtProvider.validationToken(tokenDto.getAccessToken()).get());
    }


}