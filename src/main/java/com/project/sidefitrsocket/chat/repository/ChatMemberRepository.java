package com.project.sidefitrsocket.chat.repository;

import com.project.sidefitrsocket.chat.entity.ChatMember;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ChatMemberRepository extends ReactiveCrudRepository<ChatMember, Long> {
    Flux<ChatMember> findAllByChatroomIdAndUserIdNot(Long chatroomId, Long userId);
}
