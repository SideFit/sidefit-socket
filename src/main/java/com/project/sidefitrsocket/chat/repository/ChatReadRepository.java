package com.project.sidefitrsocket.chat.repository;

import com.project.sidefitrsocket.chat.entity.ChatRead;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ChatReadRepository extends ReactiveCrudRepository<ChatRead, Long> {
    Mono<ChatRead> findByUserIdAndChatroomId(Long userId, Long chatroomId);
}
