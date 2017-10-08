package com.example.hbase_messaging.repository;

import com.example.hbase_messaging.entity.MessageEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

public interface MessageRepository {
    List<MessageEntity> get(String userIdFrom, String userIdTo);
    MessageEntity post(String userIdFrom, String userIdTo, String message);
    void seed(Seed seed);

    @Data
    @EqualsAndHashCode
    class Seed {
        private List<Message> messageList;
        private int numberOfMessages;

        @Data
        @EqualsAndHashCode
        public static class Message {
            private String userIdFrom;
            private String userIdTo;
            private String message;
        }
    }
}
