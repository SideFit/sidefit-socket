package com.project.sidefitrsocket.chat.repository;

import com.project.sidefitrsocket.chat.entity.Chat;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ChatRepository extends ReactiveCrudRepository<Chat, Long> {
    Mono<Chat> findFirstByChatroomIdOrderByIdDesc(Long chatroomId);
}
