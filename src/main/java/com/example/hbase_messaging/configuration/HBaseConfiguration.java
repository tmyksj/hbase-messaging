package com.example.hbase_messaging.configuration;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HBaseConfiguration {

    @Value("${hbase.zookeeper.quorum}")
    private String zkQuorum;

    @Value("${hbase.zookeeper.port}")
    private int zkPort;

    @Bean(name = "hbase")
    public org.apache.hadoop.conf.Configuration hbaseConfiguration() {
        org.apache.hadoop.conf.Configuration configuration =
                org.apache.hadoop.hbase.HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", zkQuorum.trim());
        configuration.set("hbase.zookeeper.port", String.valueOf(zkPort));
        return configuration;
    }

    @Bean
    public Connection connection(
            @Qualifier("hbase") org.apache.hadoop.conf.Configuration configuration) throws Exception {
        return ConnectionFactory.createConnection(configuration);
    }

}
