package com.example.hbase_messaging.service.impl;

import com.example.hbase_messaging.entity.MessageEntity;
import com.example.hbase_messaging.repository.MessageRepository;
import com.example.hbase_messaging.request.messages.GetMessagesRequest;
import com.example.hbase_messaging.request.messages.PostMessageRequest;
import com.example.hbase_messaging.response.messages.GetMessagesResponse;
import com.example.hbase_messaging.response.messages.PostMessageResponse;
import com.example.hbase_messaging.service.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MessagesServiceImpl implements MessagesService {

    private MessageRepository messageRepository;

    @Autowired
    public MessagesServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public GetMessagesResponse get(GetMessagesRequest request) {
        List<MessageEntity> entityList =
                messageRepository.get(request.getUserIdFrom(), request.getUserIdTo());

        GetMessagesResponse response = new GetMessagesResponse();
        response.setMessages(entityList);

        return response;
    }

    @Override
    public PostMessageResponse post(PostMessageRequest request) {
        MessageEntity entity =
                messageRepository.post(request.getUserIdFrom(), request.getUserIdTo(), request.getMessage());

        PostMessageResponse response = new PostMessageResponse();
        response.setMessage(entity);

        return response;
    }
}
