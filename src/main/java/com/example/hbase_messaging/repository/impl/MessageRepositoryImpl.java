package com.example.hbase_messaging.repository.impl;

import com.example.hbase_messaging.entity.MessageEntity;
import com.example.hbase_messaging.repository.MessageRepository;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class MessageRepositoryImpl implements MessageRepository {

    private static final TableName TABLE_NAME = TableName.valueOf("messages");
    private static final byte[] FAMILY = Bytes.toBytes("f");
    private static final byte[] QUALIFIER = Bytes.toBytes("q");

    private static final int MAX_VERSIONS = 65536;

    private Connection connection;

    private static final String DESCRIPTOR = ".";

    @Autowired
    public MessageRepositoryImpl(Connection connection) throws Exception {
        this.connection = connection;
        initialize();
    }

    private void initialize() throws Exception {
        try (Admin admin = connection.getAdmin()) {
            if (!admin.tableExists(TABLE_NAME)) {
                admin.createTable(TableDescriptorBuilder.newBuilder(TABLE_NAME)
                        .addColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(FAMILY)
                                .setMaxVersions(MAX_VERSIONS)
                                .build())
                        .build());
            }
        }
    }

    @Override
    public List<MessageEntity> get(String userIdFrom, String userIdTo) {
        if (userIdFrom.contains(DESCRIPTOR) || userIdTo.contains(DESCRIPTOR)) {
            return null;
        }

        try (Table table = connection.getTable(TABLE_NAME)) {
            byte[] row = Bytes.toBytes(userIdFrom + DESCRIPTOR + userIdTo);

            Get get = new Get(row)
                    .addColumn(FAMILY, QUALIFIER)
                    .readVersions(MAX_VERSIONS);

            Result result = table.get(get);

            if (result.isEmpty()) {
                return Collections.emptyList();
            } else {
                return result.getMap().get(FAMILY).get(QUALIFIER).entrySet().stream().map((entry) -> {
                    MessageEntity entity = new MessageEntity();
                    entity.setFrom(userIdFrom);
                    entity.setTo(userIdTo);
                    entity.setMessage(Bytes.toString(entry.getValue()));
                    entity.setTimestamp(entry.getKey());
                    return entity;
                }).collect(Collectors.toList());
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public MessageEntity post(String userIdFrom, String userIdTo, String message) {
        if (userIdFrom.contains(DESCRIPTOR) || userIdTo.contains(DESCRIPTOR)) {
            return null;
        }

        try (Table table = connection.getTable(TABLE_NAME)) {
            long timestamp = System.currentTimeMillis();

            Put put = new Put(Bytes.toBytes(userIdFrom + DESCRIPTOR + userIdTo));
            put.addColumn(FAMILY, QUALIFIER, timestamp, Bytes.toBytes(message));

            table.put(put);

            MessageEntity entity = new MessageEntity();
            entity.setFrom(userIdFrom);
            entity.setTo(userIdTo);
            entity.setMessage(message);
            entity.setTimestamp(timestamp);

            return entity;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void seed(Seed seed) {
        try {
            seed(seed.getNumberOfMessages());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void seed(int numberOfMessages) throws Exception {
        int size = 100;

        for (int i = 0; i < numberOfMessages / size; i++) {
            List<String> fromUserIdList = generateUserIdList(size);
            List<String> toUserIdList = generateUserIdList(size);

            try (Table table = connection.getTable(TABLE_NAME)) {
                for (int j = 0; j < size; j++) {
                    String fromUserId = fromUserIdList.get((int)(Math.random() * size));
                    String toUserId = toUserIdList.get((int)(Math.random() * size));
                    long timestamp = System.currentTimeMillis();

                    Put put = new Put(Bytes.toBytes(fromUserId + DESCRIPTOR + toUserId));
                    put.addColumn(FAMILY, QUALIFIER, timestamp, Bytes.toBytes("messages"));

                    table.put(put);
                }
            }
        }
    }

    private List<String> generateUserIdList(int length) {
        return Stream.generate(() ->
                Stream.generate(() ->
                        Character.toString("abcdefghijklmnopqrstuvwxyz".charAt((int)(Math.random() * 26))))
                        .limit(12)
                        .collect(Collectors.joining()))
                .limit(length).collect(Collectors.toList());
    }
}
