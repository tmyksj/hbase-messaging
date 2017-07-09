package com.example.hbase_messaging.request.messages;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

@Data
@EqualsAndHashCode
public class PostMessageRequest {
    @NotBlank
    private String userIdFrom;
    @NotBlank
    private String userIdTo;
    @NotBlank
    private String message;
}
