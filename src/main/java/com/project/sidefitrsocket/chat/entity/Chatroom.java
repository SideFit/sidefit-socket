package com.project.sidefitrsocket.chat.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Builder
@Table("chatroom")
public class Chatroom {
    @Id
    private final Long id;
    private String title;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;
}
