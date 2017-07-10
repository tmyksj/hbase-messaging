package com.example.hbase_messaging.controller;

import com.example.hbase_messaging.request.messages.GetMessagesRequest;
import com.example.hbase_messaging.request.messages.PostMessageRequest;
import com.example.hbase_messaging.response.messages.GetMessagesResponse;
import com.example.hbase_messaging.response.messages.PostMessageResponse;
import com.example.hbase_messaging.service.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessagesController {

    private MessagesService messagesService;

    @Autowired
    public MessagesController(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @RequestMapping(path = "/messages/{userIdFrom}/{userIdTo}", method = RequestMethod.GET)
    public GetMessagesResponse get(@Validated GetMessagesRequest request) {
        return messagesService.get(request);
    }

    @RequestMapping(path = "/messages/{userIdFrom}/{userIdTo}", method = RequestMethod.POST)
    public PostMessageResponse post(@Validated PostMessageRequest request) {
        return messagesService.post(request);
    }
}
