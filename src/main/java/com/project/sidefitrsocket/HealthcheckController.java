package com.project.sidefitrsocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class HealthcheckController {

    @MessageMapping("healthcheck")
    public Mono<String> healthcheck(Mono<String> stringMono) {
      return stringMono.doOnNext(System.out::println)
              .thenReturn("service is health");
    }

}
