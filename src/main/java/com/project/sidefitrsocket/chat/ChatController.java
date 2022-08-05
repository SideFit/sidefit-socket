package com.project.sidefitrsocket.chat;

import com.project.sidefitrsocket.chat.request.CreateChatroomRequest;
import com.project.sidefitrsocket.chat.request.MessageRequest;
import com.project.sidefitrsocket.chat.response.ChatRoomListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequiredArgsConstructor
@MessageMapping("chat")
public class ChatController {
    private final ChatService chatService;

    // 채팅방 생성
    @MessageMapping("room.create")
    public Mono<Long> createChatroom(Mono<CreateChatroomRequest> requestBody) {
        return requestBody.flatMap(chatService::createChatroom);
    }

    // 채팅 보내기
    @MessageMapping("send")
    public Mono<String> sendMessage(Mono<MessageRequest> requestBody) {
        return requestBody.flatMap(chatService::sendMessage);
    }

    @MessageMapping("room.list")
    public Flux<ChatRoomListResponse> chatroomList(Mono<String> requestBody, RSocketRequester requester) {
        requestBody.doOnNext(log::info).subscribe();
        return chatService.chatroomList(requester);
    }





}
