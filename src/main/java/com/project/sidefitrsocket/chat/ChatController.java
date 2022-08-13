package com.project.sidefitrsocket.chat;

import com.project.sidefitrsocket.acceptor.ClientManager;
import com.project.sidefitrsocket.chat.request.ChatReadRequest;
import com.project.sidefitrsocket.chat.request.CreateChatroomRequest;
import com.project.sidefitrsocket.chat.request.SendMessageRequest;
import com.project.sidefitrsocket.chat.response.ChatRoomListResponse;
import com.project.sidefitrsocket.exception.SocketAcceptorException;
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
    private final ClientManager clientManager;

    // 채팅방 생성
    @MessageMapping("room.create")
    public Mono<Long> createChatroom(Mono<CreateChatroomRequest> requestBody, RSocketRequester rSocketRequester) {
        if (clientManager.getUserIdBySocket(rSocketRequester) == null) {
            throw new SocketAcceptorException();
        }

        return requestBody.flatMap(chatService::createChatroom);
    }

    // 채팅 보내기
    @MessageMapping("send")
    public Mono<String> sendMessage(Mono<SendMessageRequest> requestBody, RSocketRequester rSocketRequester) {
        if (clientManager.getUserIdBySocket(rSocketRequester) == null) {
            throw new SocketAcceptorException();
        }

        return requestBody.flatMap(request -> chatService.sendMessage(request, rSocketRequester));
    }

    @MessageMapping("room.list")
    public Flux<ChatRoomListResponse> chatroomList(Mono<String> requestBody, RSocketRequester rSocketRequester) {
        if (clientManager.getUserIdBySocket(rSocketRequester) == null) {
            throw new SocketAcceptorException();
        }

        requestBody.doOnNext(log::info).subscribe();
        return chatService.chatroomList(rSocketRequester);
    }

    @MessageMapping("read")
    public Mono<String> chatRead(Mono<ChatReadRequest> requestBody, RSocketRequester rSocketRequester) {
        if (clientManager.getUserIdBySocket(rSocketRequester) == null) {
            throw new SocketAcceptorException();
        }

        return requestBody.flatMap(request -> chatService.chatRead(request, rSocketRequester));
    }





}
