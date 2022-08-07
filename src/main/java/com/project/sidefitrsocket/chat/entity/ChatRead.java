package com.project.sidefitrsocket.chat.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder(toBuilder = true)
@Table("chat_read")
public class ChatRead {
    private final Long id;
    private final Long userId;
    private final Long chatroomId;
    private Long chatId;

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}
