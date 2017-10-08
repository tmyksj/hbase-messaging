package com.example.hbase_messaging.service;

import com.example.hbase_messaging.request.seeds.PostSeedsRequest;

public interface SeedsService {
    void post(PostSeedsRequest request);
}
