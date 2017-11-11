package com.example.hbase_messaging.repository;

import com.example.hbase_messaging.entity.MessageEntity;

import java.util.List;

public interface MessageRepository {
    List<MessageEntity> get(String userIdFrom, String userIdTo);
    MessageEntity post(String userIdFrom, String userIdTo, String message);
    void seed(int numberOfMessages);
}
