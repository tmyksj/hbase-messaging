package com.example.hbase_messaging.repository;

import com.example.hbase_messaging.entity.MessageEntity;

import java.util.List;

public interface MessageRepository {
    List<MessageEntity> getInbox(String userId);
    List<MessageEntity> getOutbox(String userId);
    MessageEntity post(String userIdFrom, String userIdTo, String message);
}
