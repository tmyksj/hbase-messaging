package com.example.hbase_messaging.response.messages;

import com.example.hbase_messaging.entity.MessageEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class GetMessagesResponse {
    List<MessageEntity> messages;
}
