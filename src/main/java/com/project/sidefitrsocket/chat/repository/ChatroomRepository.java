package com.project.sidefitrsocket.chat.repository;

import com.project.sidefitrsocket.chat.entity.Chatroom;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomRepository extends ReactiveCrudRepository<Chatroom, Long> {
}
