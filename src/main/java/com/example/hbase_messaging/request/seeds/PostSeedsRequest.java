package com.example.hbase_messaging.request.seeds;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class PostSeedsRequest {
    private int numberOfMessages;
}
