package com.project.sidefitrsocket.chat.repository.query;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatQuery {
    private final DatabaseClient client;

    public DatabaseClient.GenericExecuteSpec findChatRoomList(Long userId) {
        String query = "" +
                "select " +
                "cr.id as chatroom_id " +
                ", (select created_date from chat where chat.id = max(c.id)) as last_message_time " +
                ", (select message from chat where chat.id = max(c.id)) as last_message " +
                ", c.`type` " +
                ", u.nickname " +
                ", i.image_url " +
                "from " +
                "chatroom cr " +
                "join chat_member cm on cm.chatroom_id = cr.id " +
                "join chat c on c.chatroom_id = cr.id " +
                "join users u on cm.user_id = u.id " +
                "join image i on u.image_id = i.id " +
                "where cm.user_id = :userId " +
                "group by cr.id"
                ;

        DatabaseClient.GenericExecuteSpec sql = this.client.sql(query).bind("userId", userId);

        return sql;
    }
}
