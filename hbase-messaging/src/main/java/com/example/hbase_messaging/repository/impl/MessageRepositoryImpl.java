package com.example.hbase_messaging.repository.impl;

import com.example.hbase_messaging.entity.MessageEntity;
import com.example.hbase_messaging.repository.MessageRepository;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MessageRepositoryImpl implements MessageRepository {

    private static final String TABLE_NAME = "messages";
    private static final String DESCRIPTOR = ".";

    private static final byte[] FAMILY = Bytes.toBytes("f");
    private static final byte[] QUALIFIER = Bytes.toBytes("q");

    private static final int MAX_VERSIONS = 8192;
    private static final int MAX_VERSIONS_PER_QUERY = 64;

    private HbaseTemplate hbaseTemplate;
    private HBaseAdmin admin;

    @Autowired
    public MessageRepositoryImpl(
            @Qualifier("hbase") Configuration configuration, HbaseTemplate hbaseTemplate) throws Exception {
        this.admin = (HBaseAdmin) ConnectionFactory.createConnection(configuration).getAdmin();
        this.hbaseTemplate = hbaseTemplate;

        initialize();
    }

    private void initialize() throws Exception {
        TableName tableName = TableName.valueOf(TABLE_NAME);

        if (!admin.tableExists(tableName)) {
            HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
            HColumnDescriptor columnDescriptor = new HColumnDescriptor(FAMILY);
            columnDescriptor.setMaxVersions(MAX_VERSIONS);
            tableDescriptor.addFamily(columnDescriptor);

            admin.createTable(tableDescriptor);
        }
    }

    @Override
    public List<MessageEntity> get(String userIdFrom, String userIdTo) {
        byte[] row = Bytes.toBytes(userIdFrom + DESCRIPTOR + userIdTo);

        Scan scan = new Scan()
                .setStartRow(row)
                .setStopRow(row)
                .addColumn(FAMILY, QUALIFIER)
                .setMaxVersions(MAX_VERSIONS_PER_QUERY);

        List<List<MessageEntity>> entityList = hbaseTemplate.find(TABLE_NAME, scan, (result, rowNum) ->
                result.listCells().stream().map(cell -> {
                    MessageEntity entity = new MessageEntity();
                    entity.setFrom(userIdFrom);
                    entity.setTo(userIdTo);
                    entity.setMessage(Bytes.toString(CellUtil.cloneValue(cell)));
                    entity.setTimestamp(cell.getTimestamp());
                    return entity;
                }).collect(Collectors.toList()));

        return entityList.size() == 1 ? entityList.get(0) : Collections.emptyList();
    }

    @Override
    public MessageEntity post(String userIdFrom, String userIdTo, String message) {
        if (userIdFrom.contains(DESCRIPTOR) || userIdTo.contains(DESCRIPTOR)) {
            return null;
        }

        return hbaseTemplate.execute(TABLE_NAME, table -> {
            MessageEntity entity = new MessageEntity();
            entity.setFrom(userIdFrom);
            entity.setTo(userIdTo);
            entity.setMessage(message);

            Put put = new Put(Bytes.toBytes(userIdFrom + DESCRIPTOR + userIdTo));
            put.addColumn(FAMILY, QUALIFIER, Bytes.toBytes(message));
            table.put(put);

            entity.setTimestamp(put.getTimeStamp());

            return entity;
        });
    }
}
