package com.tpvlog.epay.cache.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerListener {

    @KafkaListener(topics = {"product-topic"}, groupId = "product-group")
    public void consumeProduct(ConsumerRecord<Integer, String> record) {
        String data = record.value();


    }

}
