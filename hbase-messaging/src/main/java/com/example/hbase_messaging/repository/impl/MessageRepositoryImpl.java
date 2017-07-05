package com.example.hbase_messaging.repository.impl;

import com.example.hbase_messaging.entity.MessageEntity;
import com.example.hbase_messaging.repository.MessageRepository;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageRepositoryImpl implements MessageRepository {

    private static final String TABLE_NAME = "messages";
    private static final String DESCRIPTOR = ".";
    private static final byte[] CF_INFO = Bytes.toBytes("cf_info");
    private static final byte[] Q_FAMILY = Bytes.toBytes("q_family");

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
            HColumnDescriptor columnDescriptor = new HColumnDescriptor(CF_INFO);
            tableDescriptor.addFamily(columnDescriptor);

            admin.createTable(tableDescriptor);
        }
    }

    @Override
    public List<MessageEntity> getInbox(String userId) {
        return null;
    }

    @Override
    public List<MessageEntity> getOutbox(String userId) {
        return null;
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
            put.addColumn(CF_INFO, Q_FAMILY, Bytes.toBytes(message));
            table.put(put);

            entity.setTimestamp(put.getTimeStamp());

            return entity;
        });
    }
}
