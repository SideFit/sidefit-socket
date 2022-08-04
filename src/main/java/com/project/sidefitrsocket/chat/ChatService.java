package com.project.sidefitrsocket.chat;

import com.project.sidefitrsocket.acceptor.ClientManager;
import com.project.sidefitrsocket.chat.entity.ChatMember;
import com.project.sidefitrsocket.chat.entity.Chatroom;
import com.project.sidefitrsocket.chat.repository.ChatMemberRepository;
import com.project.sidefitrsocket.chat.repository.ChatReadRepository;
import com.project.sidefitrsocket.chat.repository.ChatRepository;
import com.project.sidefitrsocket.chat.repository.ChatroomRepository;
import com.project.sidefitrsocket.chat.request.CreateChatroomRequest;
import com.project.sidefitrsocket.chat.request.MessageRequest;
import com.project.sidefitrsocket.chat.response.ChatRoomListResponse;
import com.project.sidefitrsocket.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final DateUtil dateUtil;
    private final ClientManager clientManager;
    private final ChatRepository chatRepository;
    private final ChatReadRepository chatReadRepository;
    private final ChatroomRepository chatroomRepository;
    private final ChatMemberRepository chatMemberRepository;

    public Mono<String> createChatroom(CreateChatroomRequest createChatRoomRequest) {
        LocalDateTime now = LocalDateTime.now();
        Chatroom chatroom = Chatroom.builder()
                .createdDate(now)
                .lastModifiedDate(now)
                .build();

        return chatroomRepository.save(chatroom)
                .flatMapMany(entity -> {
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
                }).collectList()
                .map(list -> "Success!")
                ;
    }

    public Mono<String> sendMessage(MessageRequest messageRequest) {
        log.info("sendMessage!");

        // 1. 해당 채팅방에 메시지를 보낸다.
        // 2. 보낸 이가 해당 메시지까지 읽었다는 표시를 한다.
        // 3. 해당 채팅방의 다른 사람들이 소켓에 접속중인 경우, 알림을 보낸다.
        // 4. 성공이라는 결과를 보낸다.
        messageRequest.getMessage();
        messageRequest.getChannelId();
        return null;
    }

    public Flux<ChatRoomListResponse> chatroomList(RSocketRequester requester) {
        Long userId = clientManager.getUserIdBySocket(requester);

        return chatroomRepository.findByUserIdOrderByLastModifiedDateDesc(userId)
                .flatMap(entity -> chatRepository.findFirstByChatroomIdOrderByIdDesc(entity.getId()))
                .map(entity ->
                        ChatRoomListResponse.builder()
                                .chatroomId(entity.getChatroomId())
                                .lastMessage(entity.getMessage())
                                .lastMessageTime(
                                        dateUtil.localDateTimeToUnixTime(entity.getCreatedDate())
                                )
                            .build()
                );
    }
}
