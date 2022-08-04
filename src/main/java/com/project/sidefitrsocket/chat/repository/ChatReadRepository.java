package com.project.sidefitrsocket.chat.repository;

import com.project.sidefitrsocket.chat.entity.ChatRead;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatReadRepository extends ReactiveCrudRepository<ChatRead, Long> {
}
