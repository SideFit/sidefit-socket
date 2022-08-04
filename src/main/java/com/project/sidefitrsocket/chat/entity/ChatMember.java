package com.project.sidefitrsocket.chat.entity;


import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Builder
@Table("chat_member")
public class ChatMember {

    @Id
    private final Long id;
    private final Long chatroomId;
    private final Long userId;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;

}
