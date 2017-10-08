package com.example.hbase_messaging.request.seeds;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class PostSeedsRequest {
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
