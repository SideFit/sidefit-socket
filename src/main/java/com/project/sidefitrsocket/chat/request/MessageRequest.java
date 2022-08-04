package com.project.sidefitrsocket.chat.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageRequest {
    private final Long channelId;
    private final String message;
}
