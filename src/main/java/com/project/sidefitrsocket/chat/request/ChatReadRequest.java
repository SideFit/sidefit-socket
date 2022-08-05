package com.project.sidefitrsocket.chat.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatReadRequest {
    private final Long chatId;
    private final Long chatroomId;
}
