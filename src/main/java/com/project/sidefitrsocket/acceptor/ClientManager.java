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
    private final Map<Long, RSocketRequester> idToSocket = Collections.synchronizedMap(new HashMap<>());
    private final Map<RSocketRequester, Long> socketToId = Collections.synchronizedMap(new HashMap<>());

    public void add(String token, RSocketRequester rSocketRequester) {
        String jwt = token.replaceFirst("Bearer ", "");

        rSocketRequester.rsocket()
                .onClose()
                .doFirst(() -> {
                    log.info("start");
                    Long userId = jwtProvider.getAuthentication(jwt);
                    if (userId == 0L) throw new RuntimeException();

                    this.idToSocket.put(userId, rSocketRequester);
                    this.socketToId.put(rSocketRequester, userId);
                })
                .doFinally(s -> {
                    log.info("finally");
                    Long userId = socketToId.get(rSocketRequester);

                    socketToId.remove(rSocketRequester);
                    idToSocket.remove(userId);
                }).subscribe()
        ;
    }

    public Long getUserIdBySocket(RSocketRequester rSocketRequester) {
        return this.socketToId.get(rSocketRequester);
    }
}
