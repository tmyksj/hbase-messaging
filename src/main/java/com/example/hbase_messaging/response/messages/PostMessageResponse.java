package com.example.hbase_messaging.response.messages;

import com.example.hbase_messaging.entity.MessageEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class PostMessageResponse {
    private MessageEntity message;
    private long turnaroundTime;
}
