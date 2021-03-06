package com.example.hbase_messaging.repository;

import com.example.hbase_messaging.entity.MessageEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void get() {
        int size = 10;
        String from = UUID.randomUUID().toString();
        String to = UUID.randomUUID().toString();
        String message = "message";

        for (int i = 0; i < size; i++) {
            messageRepository.post(from, to, message + i);
        }

        List<MessageEntity> messageEntityList = messageRepository.get(from, to);
        assertThat(messageEntityList.size(), is(size));

        for (int i = 0; i < size; i++) {
            assertThat(messageEntityList.get(i).getFrom(), is(from));
            assertThat(messageEntityList.get(i).getTo(), is(to));
            assertThat(messageEntityList.get(i).getMessage(), is(message + (size - i - 1)));
            assertThat(messageEntityList.get(i).getTimestamp(), is(greaterThan(0L)));

            if (i > 0) {
                assertThat(messageEntityList.get(i).getTimestamp(),
                        is(lessThanOrEqualTo(messageEntityList.get(i - 1).getTimestamp())));
            }
        }
    }

    @Test
    public void post() {
        String from = UUID.randomUUID().toString();
        String to = UUID.randomUUID().toString();
        String message = "message";

        MessageEntity entity = messageRepository.post(from, to, message);
        assertThat(entity, is(not(nullValue())));
        assertThat(entity.getFrom(), is(from));
        assertThat(entity.getTo(), is(to));
        assertThat(entity.getMessage(), is(message));
        assertThat(entity.getTimestamp(), is(greaterThan(0L)));
        assertThat(entity.getTimestamp(), is(lessThanOrEqualTo(System.currentTimeMillis())));
    }
}
