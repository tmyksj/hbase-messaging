package com.example.hbase_messaging.config;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.hbase.HbaseConfigurationFactoryBean;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

@Configuration
public class HbaseConfig {

    @Value("${hbase.zk.quorum}")
    private String zkQuorum;

    @Value("${hbase.zk.port}")
    private int zkPort;

    @Bean
    public HbaseConfigurationFactoryBean hbaseConfigurationFactoryBean() {
        HbaseConfigurationFactoryBean config = new HbaseConfigurationFactoryBean();
        config.setZkQuorum(zkQuorum);
        config.setZkPort(zkPort);
        return config;
    }

    @Bean
    public org.apache.hadoop.conf.Configuration configuration() {
        return HBaseConfiguration.create();
    }

    @Bean
    public HbaseTemplate hbaseTemplate(org.apache.hadoop.conf.Configuration configuration) {
        return new HbaseTemplate(configuration);
    }

}
