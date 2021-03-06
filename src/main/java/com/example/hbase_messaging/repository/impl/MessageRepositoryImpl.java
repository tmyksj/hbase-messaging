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
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Repository
public class MessageRepositoryImpl implements MessageRepository {

    private static final TableName TABLE_NAME = TableName.valueOf("messages");
    private static final byte[] FAMILY = Bytes.toBytes("f");
    private static final byte[] QUALIFIER = Bytes.toBytes("q");

    private static final int MAX_VERSIONS = 65536;

    private static final String DELIMITER = "::";

    private Connection connection;

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
        if (userIdFrom.contains(DELIMITER) || userIdTo.contains(DELIMITER)) {
            return null;
        }

        try (Table table = connection.getTable(TABLE_NAME)) {
            byte[] row = Bytes.toBytes(userIdFrom + DELIMITER + userIdTo);

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
        if (userIdFrom.contains(DELIMITER) || userIdTo.contains(DELIMITER)) {
            return null;
        }

        try (Table table = connection.getTable(TABLE_NAME)) {
            long timestamp = System.currentTimeMillis();

            Put put = new Put(Bytes.toBytes(userIdFrom + DELIMITER + userIdTo));
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
    public void seed(int numberOfMessages) {
        int userIdSizePerSeed = 8192;

        try (Table table = connection.getTable(TABLE_NAME)) {
            List<String> userIdList = generateUserIdList(userIdSizePerSeed);
            long beginTime = System.currentTimeMillis() - numberOfMessages;

            table.put(IntStream.range(0, numberOfMessages).mapToObj(i -> {
                String fromUserId = userIdList.get((int)(Math.random() * userIdSizePerSeed));
                String toUserId = userIdList.get((int)(Math.random() * userIdSizePerSeed));
                long timestamp = beginTime + i;

                Put put = new Put(Bytes.toBytes(fromUserId + DELIMITER + toUserId));
                put.addColumn(FAMILY, QUALIFIER, timestamp, Bytes.toBytes("messages"));

                return put;
            }).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> generateUserIdList(int length) {
        return Stream.generate(() ->
                Stream.generate(() ->
                        Character.toString("abcdefghijklmnopqrstuvwxyz".charAt((int)(Math.random() * 26))))
                        .limit(8)
                        .collect(Collectors.joining()))
                .limit(length).collect(Collectors.toList());
    }
}
