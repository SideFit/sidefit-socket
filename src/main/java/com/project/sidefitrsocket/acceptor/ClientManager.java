package com.project.sidefitrsocket.acceptor;

import com.project.sidefitrsocket.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientManager {
    private final JwtProvider jwtProvider;
    private final Map<Long, RSocketRequester> map = Collections.synchronizedMap(new HashMap<>());

    public void add(String token, RSocketRequester rSocketRequester) {
        log.info("login user: {}", map.size());

        String jwt = token.replaceFirst("Bearer ", "");

        rSocketRequester.rsocket()
                .onClose()
                .doFirst(() -> {
                    Long userId = jwtProvider.getAuthentication(jwt);
                    if (userId == 0L) throw new RuntimeException();
                    this.map.put(userId, rSocketRequester);
                })
                .doFinally(s -> {
                    System.out.println("finally");

                    Long memberId = map.keySet().stream()
                            .filter(key -> map.get(key).equals(rSocketRequester))
                            .collect(Collectors.toList())
                            .get(0);

                    map.remove(memberId);
                }).subscribe()
        ;

        log.info("login user: {}", map.size());
    }
}
