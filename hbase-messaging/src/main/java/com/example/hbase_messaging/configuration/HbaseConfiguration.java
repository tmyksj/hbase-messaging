package com.example.hbase_messaging.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

@Configuration
public class HbaseConfiguration {

    @Value("${hbase.zookeeper.quorum}")
    private String zkQuorum;

    @Value("${hbase.zookeeper.port}")
    private int zkPort;

    @Bean(name = "hbase")
    public org.apache.hadoop.conf.Configuration hbaseConfiguration(
            @Qualifier("hadoop") org.apache.hadoop.conf.Configuration hadoopConfiguration) {
        org.apache.hadoop.conf.Configuration configuration =
                new org.apache.hadoop.conf.Configuration(hadoopConfiguration);
        configuration.set("hbase.zookeeper.quorum", zkQuorum);
        configuration.set("hbase.zookeeper.port", String.valueOf(zkPort));
        return configuration;
    }

    @Bean
    public HbaseTemplate hbaseTemplate(
            @Qualifier("hbase") org.apache.hadoop.conf.Configuration configuration) {
        return new HbaseTemplate(configuration);
    }

}
