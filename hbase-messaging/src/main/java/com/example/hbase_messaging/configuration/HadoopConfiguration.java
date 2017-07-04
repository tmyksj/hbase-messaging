package com.example.hbase_messaging.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.configuration.ConfigurationFactoryBean;

@Configuration
public class HadoopConfiguration {

    @Value("${fs.defaultFS}")
    private String defaultFs;

    @Bean
    public ConfigurationFactoryBean configurationFactoryBean() {
        ConfigurationFactoryBean config = new ConfigurationFactoryBean();
        config.setFileSystemUri(defaultFs);
        return config;
    }

}
