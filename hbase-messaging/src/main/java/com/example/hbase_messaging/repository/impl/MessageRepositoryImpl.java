package com.example.hbase_messaging.repository.impl;

import com.example.hbase_messaging.entity.MessageEntity;
import com.example.hbase_messaging.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageRepositoryImpl implements MessageRepository {
    @Override
    public List<MessageEntity> getInbox(String userId) {
        return null;
    }

    @Override
    public List<MessageEntity> getOutbox(String userId) {
        return null;
    }

    @Override
    public MessageEntity post(String userIdFrom, String userIdTo, String message) {
        return null;
    }
}
