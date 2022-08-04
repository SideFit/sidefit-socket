package com.project.sidefitrsocket.chat.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CreateChatRoomRequest {
    private final List<Long> userList;
}
