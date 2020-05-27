package com.tpvlog.epay.cache.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
@ConfigurationProperties("kafka.topic")
public class KafkaConfig {
    private String groupId;
    private String[] topicNames;


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String[] getTopicNames() {
        return topicNames;
    }

    public void setTopicNames(String[] topicNames) {
        this.topicNames = topicNames;
    }
}
