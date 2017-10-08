package com.example.hbase_messaging.service.impl;

import com.example.hbase_messaging.repository.MessageRepository;
import com.example.hbase_messaging.request.seeds.PostSeedsRequest;
import com.example.hbase_messaging.service.SeedsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
public class SeedsServiceImpl implements SeedsService {

    private MessageRepository messageRepository;

    @Autowired
    public SeedsServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void post(PostSeedsRequest request) {
        MessageRepository.Seed seed = new MessageRepository.Seed();

        if (request.getMessageList() != null) {
            seed.setMessageList(request.getMessageList().stream().map((src) -> {
                MessageRepository.Seed.Message dist = new MessageRepository.Seed.Message();
                dist.setUserIdFrom(src.getUserIdFrom());
                dist.setUserIdTo(src.getUserIdTo());
                dist.setMessage(src.getMessage());
                return dist;
            }).collect(Collectors.toList()));
        }

        seed.setNumberOfMessages(request.getNumberOfMessages());

        messageRepository.seed(seed);
    }
}