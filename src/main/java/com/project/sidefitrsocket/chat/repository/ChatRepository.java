package com.project.sidefitrsocket.chat.repository;

import com.project.sidefitrsocket.chat.entity.Chat;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends ReactiveCrudRepository<Chat, Long> {
}
