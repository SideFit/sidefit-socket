package com.project.sidefitrsocket.chat;

import com.project.sidefitrsocket.chat.request.CreateChatRoomRequest;
import com.project.sidefitrsocket.chat.request.MessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    // 채팅방 생성
    @MessageMapping("create/chat/room")
    public Mono<String> createChatRoom(Mono<CreateChatRoomRequest> requestBody) {
        return requestBody.flatMap(chatService::createChatRoom);
    }

    // 채팅 보내기
    @MessageMapping("send/message")
    public Mono<String> sendMessage(Mono<MessageRequest> requestBody) {
        return requestBody.flatMap(chatService::sendMessage);
    }





}
