package com.example.hbase_messaging.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HadoopConfiguration {

    @Value("${fs.defaultFS}")
    private String defaultFs;

    @Bean(name = "hadoop")
    public org.apache.hadoop.conf.Configuration hadoopConfiguration() {
        org.apache.hadoop.conf.Configuration configuration = new org.apache.hadoop.conf.Configuration();
        configuration.set("fs.defaultFS", defaultFs.trim());
        return configuration;
    }

}
