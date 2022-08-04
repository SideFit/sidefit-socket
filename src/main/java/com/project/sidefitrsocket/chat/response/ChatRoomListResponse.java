package com.project.sidefitrsocket.chat.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomListResponse {
    private final Long chatroomId;
    private final String lastMessage;
    private final Long lastMessageTime;
}
