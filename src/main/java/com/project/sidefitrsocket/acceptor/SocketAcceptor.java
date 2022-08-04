package com.project.sidefitrsocket.acceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SocketAcceptor {
    private final ClientManager clientManager;

    @ConnectMapping("socket.acceptor")
    public Mono<Void> socketAcceptor(
            String token,
            RSocketRequester rSocketRequester) {
        log.info("socket.acceptor");
        return Mono.fromRunnable(() -> this.clientManager.add(token, rSocketRequester));
    }
}
