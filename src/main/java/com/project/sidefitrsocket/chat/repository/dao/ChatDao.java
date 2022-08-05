package com.project.sidefitrsocket.chat.repository.dao;

import com.project.sidefitrsocket.chat.repository.query.ChatQuery;
import com.project.sidefitrsocket.chat.response.ChatRoomListResponse;
import com.project.sidefitrsocket.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ChatDao {
    private final DateUtil dateUtil;
    private final ChatQuery chatQuery;

    public Flux<ChatRoomListResponse> findChatRoomList(Long userId) {
        return this.chatQuery.findChatRoomList(userId).map(((row, rowMetadata) -> {
            Long lastMessageTime = dateUtil.localDateTimeToUnixTime(
                    Objects.requireNonNull(row.get("last_message_time", LocalDateTime.class))
            );
            return ChatRoomListResponse.builder()
                    .chatroomId(row.get("chatroom_id", Long.class))
                    .lastMessage(row.get("last_message", String.class))
                    .lastMessageTime(lastMessageTime)
                    .nickname(row.get("nickname", String.class))
                    .imageUrl(row.get("image_url", String.class))
                    .type(row.get("type", Byte.class))
                    .build();
        })).all();
    }


}
