package com.example.hbase_messaging.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class MessageEntity {
    private String from;
    private String to;
    private String message;
    private Long timestamp;
}
