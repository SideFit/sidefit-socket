package com.project.sidefitrsocket.chat;

import com.project.sidefitrsocket.acceptor.ClientManager;
import com.project.sidefitrsocket.chat.entity.Chat;
import com.project.sidefitrsocket.chat.entity.ChatMember;
import com.project.sidefitrsocket.chat.entity.ChatRead;
import com.project.sidefitrsocket.chat.entity.Chatroom;
import com.project.sidefitrsocket.chat.repository.ChatMemberRepository;
import com.project.sidefitrsocket.chat.repository.ChatReadRepository;
import com.project.sidefitrsocket.chat.repository.ChatRepository;
import com.project.sidefitrsocket.chat.repository.ChatroomRepository;
import com.project.sidefitrsocket.chat.repository.dao.ChatDao;
import com.project.sidefitrsocket.chat.request.ChatReadRequest;
import com.project.sidefitrsocket.chat.request.CreateChatroomRequest;
import com.project.sidefitrsocket.chat.request.SendMessageRequest;
import com.project.sidefitrsocket.chat.response.ChatRoomListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatDao chatDao;
    private final ClientManager clientManager;
    private final ChatRepository chatRepository;
    private final ChatReadRepository chatReadRepository;
    private final ChatroomRepository chatroomRepository;
    private final ChatMemberRepository chatMemberRepository;

    public Mono<Long> createChatroom(CreateChatroomRequest createChatRoomRequest) {
        LocalDateTime now = LocalDateTime.now();
        Chatroom chatroom = Chatroom.builder()
                .createdDate(now)
                .lastModifiedDate(now)
                .build();

        AtomicReference<Long> chatroomId = new AtomicReference<>(0L);

        return chatroomRepository.save(chatroom)
                .flatMapMany(entity -> {
                    chatroomId.set(entity.getId());
                    List<ChatMember> chatMemberList = new ArrayList<>();
                    createChatRoomRequest.getUserList().forEach(id ->
                        chatMemberList.add(
                            ChatMember.builder()
                                    .chatroomId(entity.getId())
                                    .userId(id)
                                    .createdDate(now)
                                    .lastModifiedDate(now)
                                    .build()
                        )
                    );
                    return chatMemberRepository.saveAll(chatMemberList);
                }).flatMap(entity -> {
                    ChatRead chatRead = ChatRead.builder()
                            .chatId(0L)
                            .userId(entity.getUserId())
                            .chatroomId(entity.getChatroomId())
                            .build();
                    return chatReadRepository.save(chatRead);
                }).collectList()
                .map(list -> chatroomId.get())
                ;
    }

    public Mono<String> sendMessage(SendMessageRequest requestBody, RSocketRequester rSocketRequester) {
        Long userId = clientManager.getUserIdBySocket(rSocketRequester);

        Chat chat = Chat.builder()
                .chatroomId(requestBody.getChatroomId())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .userId(userId)
                .build();

        return chatRepository.save(chat)
                .flatMap(entity ->  chatReadRepository.findByUserIdAndChatroomId(userId, entity.getChatroomId()))
                .flatMapMany(entity -> chatMemberRepository.findAllByChatroomIdAndUserIdNot(entity.getChatroomId(), userId))
                .map(ChatMember::getUserId)
                .map(clientManager::getSocketByUserId)
                .flatMap(socketOptional ->
                    socketOptional.<org.reactivestreams.Publisher<String>>map(socketRequester -> socketRequester.route("chat.receive")
                        .data(requestBody)
                        .send()
                        .thenReturn("Success!"))
                        .orElseGet(() -> Mono.just("fail!"))
                )
                .collectList().thenReturn("Success!");
    }

    public Flux<ChatRoomListResponse> chatroomList(RSocketRequester requester) {
        Long userId = clientManager.getUserIdBySocket(requester);
        return chatDao.findChatRoomList(userId);
    }

    public Mono<String> chatRead(ChatReadRequest requestBody, RSocketRequester rSocketRequester) {
        Long userId = clientManager.getUserIdBySocket(rSocketRequester);

        return chatReadRepository.findByUserIdAndChatroomId(userId, requestBody.getChatroomId())
                .flatMap(chatRead -> {
                    chatRead.setChatId(requestBody.getChatId());
                    return chatReadRepository.save(chatRead);
                }).map(entity -> "Success!")
                ;
    }
}
