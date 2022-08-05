package com.project.sidefitrsocket.chat.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SendMessageRequest {
    private final Long chatroomId;
    private final String message;
}
