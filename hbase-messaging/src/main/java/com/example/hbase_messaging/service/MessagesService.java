package com.example.hbase_messaging.service;

import com.example.hbase_messaging.request.messages.GetMessagesRequest;
import com.example.hbase_messaging.request.messages.PostMessageRequest;
import com.example.hbase_messaging.response.messages.GetMessagesResponse;
import com.example.hbase_messaging.response.messages.PostMessageResponse;

public interface MessagesService {
    GetMessagesResponse get(GetMessagesRequest request);
    PostMessageResponse post(PostMessageRequest request);
}
